package com.revature.pms.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.pms.exceptions.InsufficientBalance;
import com.revature.pms.exceptions.UserException;
import com.revature.pms.exceptions.MinimumAmountException;
import com.revature.pms.model.Customer;
import com.revature.pms.model.Employee;
import com.revature.pms.util.DBConnection;

public class CustomerDAOImpl implements CustomerDAO{
	
	Connection connection=DBConnection.getDBConnection();
	
	Scanner sc=new Scanner(System.in);
	private final String ADD_CUSTOMER_QUERY="insert into bank.customer values(?,?,?,?,?,?)";
	private final String VALIDATE_CUSTOMER_QUERY="select * from bank.Customer where accountno=? and userPassword=?";
	private final String VIEW_CUSTOMER_BALANCE_QUERY="select * from bank.Customer where accountno=?";
	private final String UPDATE_CUSTOMER_QUERY="update bank.Customer set username=?,userpassword=?,phoneno=?,address=?,balance=? where accountno=?";
	private final String DELETE_CUSTOMER_QUERY="delete from bank.Customer where accountno=?";
	private final String VIEW_ACCOUNT_BY_ACCOUNTNO="select * from bank.Customer where accountno=?";
	private final String WITHDRAW_FROM_ACCOUNT="update bank.customer set balance=balance-? where accountno=?";
	private final String DEPOSIT_TO_ACCOUNT="update bank.customer set balance=balance+? where accountno=?";
	private final String TRANS_AFTER_WITHDRAW="insert into bank.transaction values(?,?,?)";
	private final String TRANS_AFTER_DEPOSIT="insert into bank.transaction values(?,?,0,?)";
	private final String TRANS_AFTER_TRANSFERFUND="insert into bank.transaction values(?,?,0,0,?,?)";
	private final String VIEW_STATEMENT="select c.accountno,username,aftertransbalnace,withdraw,deposit,transferfund,toaccountno,transactiondate,transtime from bank.customer c join bank.transaction t on t.accountno=c.accountno where t.accountno =?";
	
	//to create a new account 
	@Override
	public boolean createAccount(Customer customer) {
		
		
		int res = 0;
		try {
		PreparedStatement statement=connection.prepareStatement(ADD_CUSTOMER_QUERY);
		statement.setInt(1,customer.getAccountNo());
		statement.setString(2,customer.getUserName());
		statement.setString(3,customer.getUserpassword());
		statement.setString(4,customer.getPhoneNo());
		statement.setString(5,customer.getAddress());
		statement.setInt(6,customer.getBalance());
		res=statement.executeUpdate();
		}
		 catch (SQLException e) {
			
		}
	
		if(res==0)
			return false;
		else
			return true;
	}
	
	//validating account against accountno and pass
	@Override
	public Customer validateAccount(int accountNo,String userPassword) throws UserException {
		
		Customer customer=null;
		ResultSet resultSet=null;
		try {
			PreparedStatement statement=connection.prepareStatement(VALIDATE_CUSTOMER_QUERY);
			statement.setInt(1, accountNo);
			statement.setString(2,userPassword);
			resultSet=statement.executeQuery();
			
			while(resultSet.next())
			{
				customer=new Customer();
				customer.setAccountNo(resultSet.getInt(1));
				customer.setUserName(resultSet.getString(2));
				customer.setUserpassword(resultSet.getString(3));
				customer.setPhoneNo(resultSet.getString(4));
				customer.setAddress(resultSet.getString(5));
				customer.setBalance(resultSet.getInt(6));				
			}
			
				
		}
		catch (SQLException e) {
			
		}
		return customer;
		
	}
	
	//to view the balance of customer
	@Override
	public int viewBalance(int accountNo) {
		int balance=0;
		ResultSet resultSet=null;
		try {
			PreparedStatement statement=connection.prepareStatement(VIEW_CUSTOMER_BALANCE_QUERY);
			statement.setInt(1, accountNo);
			resultSet=statement.executeQuery();
			
			//to return balance
			while(resultSet.next())
			{
				balance=resultSet.getInt(6);
			}
		} 
		
		catch (SQLException e) {
			System.out.println("ERROR!!");
		}
		return balance;
	}
	
	//to update customer account
	@Override
	public boolean updateAccount(Customer customer) {
		int res=0;
		try {
			
		PreparedStatement statement=connection.prepareStatement(UPDATE_CUSTOMER_QUERY);
		statement.setString(1, customer.getUserName());
		statement.setString(2,customer.getUserpassword());
		statement.setString(3, customer.getPhoneNo());
		statement.setString(4, customer.getAddress());
		statement.setInt(5, customer.getBalance());
		statement.setInt(6, customer.getAccountNo());
		
		res=statement.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("error");
			
		}
		if(res==0)
			return false;
		else
			return true;
	}

	//to delete customer account by id
	//using for junit
	@Override
	public boolean deleteAccount(int AccountNo) {
		
		
		int res=0;
		try {
			PreparedStatement statement=connection.prepareStatement(DELETE_CUSTOMER_QUERY);
			statement.setInt(1, AccountNo);
			res = statement.executeUpdate();
			}
			 catch (SQLException e) {
				
				e.printStackTrace();
			}
		if(res==0)
			return false;
		else
			return true;
		
	}
	
	//to view the particular account by accountno
	@Override
	public List getAccountById(int AccoutNo) {
		
		List<Customer> customers=new ArrayList<Customer>();
		try{
			PreparedStatement statement=connection.prepareStatement(VIEW_ACCOUNT_BY_ACCOUNTNO);
			statement.setInt(1,AccoutNo);
			ResultSet resultSet=statement.executeQuery();	
			while(resultSet.next())
			{
				Customer customer=new Customer();
				customer.setAccountNo(resultSet.getInt(1));
				customer.setUserName(resultSet.getString(2));
				customer.setUserpassword(resultSet.getString(3));
				customer.setPhoneNo(resultSet.getString(4));
				customer.setAddress(resultSet.getString(5));
				customer.setBalance(resultSet.getInt(6));	
				customers.add(customer);
				}	
		}
		 catch (SQLException e) {
				System.out.println("ERROR!!");
			}
		
		return customers;
	}
	
	//to transfer fund from one account to other
	@Override
	public void transferAmount(int senderAccountNo, int recieverAccountNo, int amount) throws InsufficientBalance{
		
		int debitorBalance=0,creditorBalance=0;
		
		int balance=viewBalance(senderAccountNo);
			try {
				//only transferring is possible if sender balance is greater than transfer fund amount
				if(balance>amount){
				PreparedStatement statement=connection.prepareStatement("select balance from bank.Customer where accountno=?");
				statement.setInt(1, senderAccountNo);
				ResultSet set=statement.executeQuery();
				
				//calling a stored procedure 
				CallableStatement callStatement=connection.prepareCall("call bank.transferAmoundAndReturenBalance(?,?,?,?,?)");
				callStatement.setInt(1,senderAccountNo);
				callStatement.setInt(2,recieverAccountNo);
				callStatement.setInt(3,amount);
				
				//to get the output from procedure
				callStatement.registerOutParameter(4, Types.INTEGER);
				callStatement.setInt(4, debitorBalance);
				callStatement.registerOutParameter(5, Types.INTEGER);
				callStatement.setInt(5, creditorBalance);
				callStatement.execute();
				
				//balance of sender and receiver after amount transferred
				debitorBalance=callStatement.getInt(4);
				creditorBalance=callStatement.getInt(5);
				System.out.println("Debitor BALANCE : " +debitorBalance);
				System.out.println("Creditor BALANCE : " +creditorBalance);
				
				//for transaction log-- storing in transaction table
				statement=connection.prepareStatement(TRANS_AFTER_TRANSFERFUND);
				statement.setInt(1,senderAccountNo);
				statement.setInt(2,debitorBalance);
				statement.setInt(3,amount);
				statement.setInt(4,recieverAccountNo);
				statement.executeUpdate();
				}
				else
					throw new InsufficientBalance("InSufficient Balance!! to send the amount , your balance "+balance);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			
		} 
	
	//to withdraw form account
	@Override
	public void withdraw(int accountNo, int amount) throws InsufficientBalance, MinimumAmountException  {
		
		int balance=viewBalance(accountNo);
		if(balance>=amount)
		{
			//if only 500 is balance in account
			if(balance<=500)
			{
				throw new MinimumAmountException("Withdrawn Denied!! Minimum Amount in your account should be 500 and Your balance is:"+balance);
			}
			
			try {
				PreparedStatement statement=connection.prepareStatement(WITHDRAW_FROM_ACCOUNT);
				statement.setInt(1, amount);
				statement.setInt(2, accountNo);	
				statement.executeUpdate();
				balance=viewBalance(accountNo);
				System.out.println("Withdrawan successfully!!, Avaliable balance :"+balance);
				
				//for transaction log-- storing in transaction table
				statement=connection.prepareStatement(TRANS_AFTER_WITHDRAW);
				statement.setInt(1,accountNo);
				statement.setInt(2,balance);
				statement.setInt(3,amount);
				statement.executeUpdate();
				} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		else
			throw new InsufficientBalance("InSufficient Balance!! to withdraw, your balalnce is:"+balance);
		
	}
	
	//to deposit in account
	@Override
	public void deposit(int accountNo, int amount) {
		
		try {
			PreparedStatement statement=connection.prepareStatement(DEPOSIT_TO_ACCOUNT);
			statement.setInt(1, amount);
			statement.setInt(2, accountNo);	
			statement.executeUpdate();
			int balance=viewBalance(accountNo);
			System.out.println("Deposited successfully!!, Avaliable balance :"+balance);
			
			//for transaction log-- storing in transaction table
			statement=connection.prepareStatement(TRANS_AFTER_DEPOSIT);
			statement.setInt(1,accountNo);
			statement.setInt(2,balance);
			statement.setInt(3,amount);
			statement.executeUpdate();
			} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//to check existence of account
	@Override
	public boolean isAccountExists(int accountNo)
	{
		boolean result=false;
		try {
			PreparedStatement statement=connection.prepareStatement(VIEW_ACCOUNT_BY_ACCOUNTNO);
			statement.setInt(1,accountNo);
			ResultSet resultSet=statement.executeQuery();
			if(resultSet.next())
				result=true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	//to view the transaction of customer
	@Override
	public void viewStatement(int accountno)
	{
		PreparedStatement  statement = null;
        ResultSet rs =null;
       try {
		statement=connection.prepareStatement(VIEW_STATEMENT);
		statement.setInt(1,accountno);
		DatabaseMetaData databaseMetaData=connection.getMetaData();
		rs=statement.executeQuery();
		ResultSetMetaData metaData=rs.getMetaData();
		int colCount=metaData.getColumnCount();
		
		
		//to look good :)
		String arr[]={"AccountNo","UserName","Availbal", "Withdraw"," Deposit", "Transfund","ToAccountNo","Transcation Date","Transcation Time"};
		System.out.println();
		for(int j=0;j<9;j++)
		{
			System.out.print(arr[j]);
			System.out.print("   ");
		}
		System.out.println();
		while(rs.next())
		{
			for(int i=1;i<=colCount;i++)
			{
				String len=rs.getString(i);
				
				if(rs.getString(i).equals("0"))
					System.out.print("----");
				
				else{
					System.out.print(rs.getString(i));
					}
				for(int j=1;j<5;j++)
				{
						System.out.print("  ");
				}
			}
			System.out.println();
		}
       } 
       catch (SQLException e) {
    	   e.printStackTrace();
	}
        	  
	}
	
	
}
