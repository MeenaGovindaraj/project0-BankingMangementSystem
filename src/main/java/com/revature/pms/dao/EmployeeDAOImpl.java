package com.revature.pms.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.pms.exceptions.UserException;
import com.revature.pms.model.Customer;
import com.revature.pms.model.Employee;
import com.revature.pms.util.DBConnection;
import com.revature.pms.model.*;
public class EmployeeDAOImpl implements EmployeeDAO{

	Connection connection=DBConnection.getDBConnection();
	private final String ADD_EMPLOYEE_QUERY="insert into bank.employee values(?,?,?,?,?)";
	private final String VIEW_EMPLOYEE_BY_EMPLOYEEID="select * from bank.employee where employeeId=?";
	private final String VALIDATE_EMPLOYEE_QUERY="select * from bank.employee where employeeId=? and employeepassword=?";
	private final String UPDATE_EMPLOYEE_QUERY="update bank.employee set employeename=?,employeepassword=?,phoneno=?,address=? where employeeId=?";
	private final String DELETE_EMPLOYEE_QUERY="delete from bank.employee where employeeId=?";
	private final String VIEW_ALL_CUSTOMERS="select * from bank.customer";
	private final String VIEW_CUSTOMER_LOG_BY_ID="select c.accountno,username,aftertransbalnace,withdraw,deposit,transferfund,toaccountno,transactiondate,transtime from bank.customer c join bank.transaction t on t.accountno=c.accountno where t.accountno =?";
	private final String VIEW_ALL_CUSTOMER_LOG="select c.accountno,username,aftertransbalnace,withdraw,deposit,transferfund,toaccountno,transactiondate,transtime from bank.customer c join bank.transaction t on t.accountno=c.accountno";
	
	
	Employee employee=null;
	
	//TO CHECK THE EXISTENCE OF ACCOUNT
	@Override
	public boolean isEmployeeExists(int employeeId) {
		
		boolean result=false;
		try {
			PreparedStatement statement=connection.prepareStatement(VIEW_EMPLOYEE_BY_EMPLOYEEID);
			statement.setInt(1, employeeId);
			ResultSet resultSet=statement.executeQuery();
			
			//if exists return true
			if(resultSet.next())
				result=true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	//to create a new account 
	@Override
	public boolean createAccount(Employee employee) {
		
		int res = 0;
		try {
		PreparedStatement statement=connection.prepareStatement(ADD_EMPLOYEE_QUERY);
		statement.setInt(1,employee.getemployeeId());
		statement.setString(2,employee.getemployeeName());
		statement.setString(3,employee.getemployeePassword());
		statement.setString(4,employee.getPhoneNo());
		statement.setString(5,employee.getAddress());
		res=statement.executeUpdate();
		}
		 catch (SQLException e) {
		}
		//if account is not created
		if(res==0)
			return false;
		else
			return true;
		
	}
	
	@Override
	//validating employee account against id and pass
	public Employee validateAccount(int employeeId, String employeePassword) throws UserException {
		
		
		ResultSet resultSet=null;
		try {
			PreparedStatement statement=connection.prepareStatement(VALIDATE_EMPLOYEE_QUERY);
			statement.setInt(1,employeeId );
			statement.setString(2,employeePassword);
			resultSet=statement.executeQuery();
			
			//for other operations retriving datas of employee
			while(resultSet.next())
			{
				employee=new Employee();
				employee.setemployeeId(resultSet.getInt(1));
				employee.setemployeeName(resultSet.getString(2));
				employee.setemployeePassword(resultSet.getString(3));
				employee.setPhoneNo(resultSet.getString(4));
				employee.setAddress(resultSet.getString(5));			
			}
			
				
		}
		catch (SQLException e) {
			
		}
		return employee;
		
	}

	//to view the particular account by employeeid
	@Override
	public List getAccountById(int employeeId) {
		
		List<Employee> employees=new ArrayList<Employee>();
		try{
			PreparedStatement statement=connection.prepareStatement(VIEW_EMPLOYEE_BY_EMPLOYEEID);
			statement.setInt(1,employeeId);
			ResultSet resultSet=statement.executeQuery();	
			
			//to store the data of employee
			while(resultSet.next())
			{
				Employee employee=new Employee();
				employee.setemployeeId(resultSet.getInt(1));
				employee.setemployeeName(resultSet.getString(2));
				employee.setemployeePassword(resultSet.getString(3));
				employee.setPhoneNo(resultSet.getString(4));
				employee.setAddress(resultSet.getString(5));	
				employees.add(employee);
				}	
		}
		 catch (SQLException e) {
				System.out.println("ERROR!!");
			}
		
		return employees;
		
	}

	//to update employee account
	@Override
	public boolean updateAccount(Employee employee) {
		
		int res=0;
		try {
			
		PreparedStatement statement=connection.prepareStatement(UPDATE_EMPLOYEE_QUERY);
		statement.setString(1, employee.getemployeeName());
		statement.setString(2,employee.getemployeePassword());
		statement.setString(3, employee.getPhoneNo());
		statement.setString(4, employee.getAddress());
		statement.setInt(5, employee.getemployeeId());
		
		res=statement.executeUpdate();
		}
		catch (SQLException e) {
			
		}
		if(res==0)
			return false;
		else
			return true;
	}
	
	//to delete employee account by id
	@Override
	public boolean deleteAccount(int employeeId) {
		
		int res=0;
		try {
			PreparedStatement statement=connection.prepareStatement(DELETE_EMPLOYEE_QUERY);
			statement.setInt(1, employeeId);
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
	
	//to view all customers in database
	@Override
	public List<Customer> viewAllCustomers() {
		
		List<Customer> customers=new ArrayList<Customer>();
		Customer customer=null;
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet res=statement.executeQuery(VIEW_ALL_CUSTOMERS);
			
			//storing customer data in a list
			while(res.next())
			{
				customer=new Customer();
				customer.setAccountNo(res.getInt(1));
				customer.setUserName(res.getString(2));
				customer.setUserpassword(res.getString(3));
				customer.setPhoneNo(res.getString(4));
				customer.setAddress(res.getString(5));
				customer.setBalance(res.getInt(6));
				customers.add(customer);
			}
		}
		catch (SQLException e) {
			
			e.printStackTrace();
		}
			
			return customers;
		
	}

	//can view the log of transactions of particular customer by accountno
	@Override
	public void viewCustomerLogById(int accountno)
	{
		PreparedStatement  statement = null;
        ResultSet rs =null;
       try {
		statement=connection.prepareStatement(VIEW_CUSTOMER_LOG_BY_ID);
		statement.setInt(1,accountno);
		DatabaseMetaData databaseMetaData=connection.getMetaData();
		rs=statement.executeQuery();
		
		//to get metadata of result on our query
		ResultSetMetaData metaData=rs.getMetaData();
		
		//to get the column count  
		int colCount=metaData.getColumnCount();
		
		//for viewing purpose and to look good
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

	//can view the log of transactions all customers
	@Override
	public void viewAllLog() {
		PreparedStatement  statement = null;
        ResultSet rs =null;
       try {
		statement=connection.prepareStatement(VIEW_ALL_CUSTOMER_LOG);
		DatabaseMetaData databaseMetaData=connection.getMetaData();
		rs=statement.executeQuery();
		ResultSetMetaData metaData=rs.getMetaData();
		int colCount=metaData.getColumnCount();
		
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
