package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.revature.pms.dao.CustomerDAO;
import com.revature.pms.dao.CustomerDAOImpl;
import com.revature.pms.exceptions.InsufficientBalance;
import com.revature.pms.exceptions.NegativeAmountException;
import com.revature.pms.exceptions.UserException;
import com.revature.pms.exceptions.MinimumAmountException;
import com.revature.pms.model.Customer;
import com.revature.pms.model.Employee;

import org.apache.log4j.Logger;

public class CustomerApp {

	Logger logger=Logger.getLogger("CustomerApp");
	Customer customer=null;
	CustomerDAO customerDAO=new CustomerDAOImpl();
	Scanner sc=new Scanner(System.in);
	
	//for creating an account
	public void startCustomer() {
		
		boolean result=false;
			try {
				customer=acceptCustomerDetails();
				//to check account Existence
				if(customerDAO.isAccountExists(customer.getAccountNo())){
					throw new UserException("\n#Sorry "+customer.getUserName()+" your Account is NOT CREATED successfully!!");
				}
				//if account doesn't exists - creating new account
				else
				{
					result=customerDAO.createAccount(customer);
					logger.info("\n#Congrats,"+ customer.getUserName()+", your bank account opened successfully!! on "+new Date());
				}
					
			} 
			catch (MinimumAmountException e) {
				logger.error("Denied!!, to create account"+e.getMessage());
			}
			catch(InputMismatchException e1)
			{
				logger.error("Input Mismatch!!- Enter Correct Input format");
			} catch (UserException e) {
				logger.error("\nAccout Already Exists!!\n"+ e.getMessage());;
			}
			
			
		
	}	
	
	//to login the account
	public void startCustomerLogin() {
		
		boolean result=false;
		//login needs account no and password..
		System.out.println("Enter ACCOUNT NUMBER:");
		int accountNo=sc.nextInt();
		System.out.println("Enter USER PASSWORD:");
		String userpassword=sc.next();
		Customer customer=new Customer();
		try {
			customer=customerDAO.validateAccount(accountNo,userpassword);
			//if there is no customer found 
			if(customer==null){
				throw new UserException("Please TRY AGAIN! INCORRECT Name/Password");
			}
			logger.info("################# Login Screen ####################");
			
			//if customer found- performing customer operations(update,delete...)
			customerOperations(customer);
		} 
		catch (UserException e) {
			logger.error(e.getMessage());
		}
		
	}


	private void customerOperations(Customer customer) {
	
		//a welcome page for customer- can perform operation as per their requirements
		try{
		logger.info("\n                  Welcome , "+customer.getUserName());
		logger.info("# # # # # # # # Personal page for " + customer.getUserName()+ " # # # # # # # ");
		int choice=0;
		boolean result=false;
		while(true)
		{

			System.out.println("\n1. View Balance");
			System.out.println("2. Update account");
			System.out.println("3. Transfer amount");
			System.out.println("4. Withdraw");
			System.out.println("5. Deposit");
			System.out.println("6. View Account");
			System.out.println("7. View Statement");
			System.out.println("8. Logout");
			System.out.println("9. Exit");
			choice=sc.nextInt();
			switch(choice)
			{
			case 1:
				logger.info("\n# Welcome to View Balance");
				int balance=0;
				//view balance with account no
					balance=customerDAO.viewBalance(customer.getAccountNo());
					logger.info("\nACCOUNT NO:"+customer.getAccountNo());
					logger.info("TOTAL BALANCE:"+balance);
				 
				break;
			case 2:
				logger.info("\n# Welcome to Update Section");
				//updating the account 
				customer=getUpdate(customer);
				
				try{
					if(customerDAO.isAccountExists(customer.getAccountNo())){	
					//update account(as per requirements like name,pass,address..)
					result=customerDAO.updateAccount(customer);
				if(result==true)
					logger.info(customer.getUserName()+ " ,Your account Updated Successfully!! on "+new Date());
				}
				else
					logger.error(customer.getUserName()+ " Your account Not Updated");
				}
				catch(InputMismatchException e1)
				{
					System.out.println("Input Mismatch!!- Enter Correct Input format");
				}
				break;
			case 3:
				logger.info("\n# Welcome to Transfer Amount");
				int senderAccountNo=customer.getAccountNo();
				logger.info("Enter Reciever Account no");
				int recieverAccountNo=sc.nextInt();
				logger.info("Enter Amount to send");
				int amount=sc.nextInt();
				if(customerDAO.isAccountExists(recieverAccountNo)){
					try {
						//valid amount
						if(amount>0){
						customerDAO.transferAmount(senderAccountNo,recieverAccountNo,amount);
						}
						else
						{
							//negative amount
							logger.error("Negavite amount can't be transferd");
						}
					} catch (InsufficientBalance e) {
						logger.error("FUND NOT TRANSFERED ,"+e.getMessage());
					} 
				
				}
				else
				{
					logger.error("Recevier account with account no: "+recieverAccountNo+", doesn't exists ");
				}
				break;
			case 4:
				logger.info("\n# Welcome to Withdraw Section");
				logger.info("Enter amount to withdraw");
				amount=sc.nextInt();
				
				try {
					//valid amount
					if(amount>0){
					customerDAO.withdraw(customer.getAccountNo(),amount);
					}
					else
					{
						//negative amount
						throw new NegativeAmountException("Negative amount cannot be withdrawn!");
					}
				}
				//insufficient balance
				catch (InsufficientBalance e) {
					logger.error(e.getMessage());
					}
				//<500 balance
				catch (MinimumAmountException e) {
					logger.error(e.getMessage());
				}
				//negative amount
				catch (NegativeAmountException e) {
					logger.error(e.getMessage());
				}
				break;
			case 5:
				logger.info("\n# Welcome to Deposit Section");
				logger.info("Enter amount to Deposit");
				amount=sc.nextInt();
				
					//valid amount
					if(amount>0){
					customerDAO.deposit(customer.getAccountNo(),amount);
					}
					else
					{
						try {
							//negative amount
							throw new NegativeAmountException("Negative amount cannot be withdrawn!");
						} 
						catch (NegativeAmountException e) {
							logger.error(e.getMessage());
						}
					}
				break;
			case 6:
				logger.info("\n# Welcome to View Account Section");
				List<Customer> customers=customerDAO.getAccountById(customer.getAccountNo());
				//if no customer with this accno
				if(customers.isEmpty()==true)
					logger.error("There is No account with "+customer.getAccountNo());
				else
					System.out.println(customers);
				break;
			case 7:
				//can we their logs
				logger.info("\n# Welcome to View Statement Section");
				customerDAO.viewStatement(customer.getAccountNo());
				break;
			case 8:
				logger.info("\n# Logout");
				logger.info("Logouted Successfully!!");
				//for home page
				App app=new App();
				app.startApp();
				break;
			case 9:
				logger.info("\nExcited!");
				logger.info("Thanks for Using this app");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Option!");
				break;
			}
		
		}
		}
		//wrong input type
		catch(InputMismatchException e)
		{
			System.out.println("Input Mismatch!!- Enter Correct Input format");
			customerOperations(customer);
		}
		
	}


	//updating account on particular fields
	private Customer getUpdate(Customer customer) throws InputMismatchException{
		int choice=0;
		Customer customer2=null;
		
		while(true){
			try{
			logger.info("Choose to Update");
			System.out.println("1. User Name");
			System.out.println("2. User Password");
			System.out.println("3. Phone Number");
			System.out.println("4. Address");
			choice=sc.nextInt();
			switch(choice)
			{
			case 1:
				//updating name
				logger.info("Enter USER NAME to update");
				String name=sc.next();
				name+=sc.nextLine();
				customer.setUserName(name);
				return customer;
				
			case 2:

				//updating password
				logger.info("Enter PASSWORD to update");
				String password=sc.next();
				customer.setUserpassword(password);
				return customer;
				
			case 3:
				//update number
				logger.info("Enter PHONE NUMBER to update");
				String phoneno=sc.next();
				customer.setPhoneNo(phoneno);
				return customer;
			
			case 4:
				//updating address
				logger.info("Enter ADDRESS to update");
				String address=sc.next();
				customer.setAddress(address);
				return customer;
			
				
			}
		}
			catch (InputMismatchException e) {
			System.out.println("Input Mismatch!!- Enter Correct Input format");
		}

		}
		
	}
	//for creating account getting details
	private Customer acceptCustomerDetails() throws MinimumAmountException,InputMismatchException {
		
		System.out.println("Please Enter ACCOUNT NO:");
		int accountNo=sc.nextInt();
		System.out.println("Please Enter Your NAME:");
		String userName=sc.next();
		userName+=sc.nextLine();
		System.out.println("Set a password [ Minimum 6 chars with upper, lower and digts and special charcters ]");
		String  userPassword = sc.next();
		//password validation
		while (!userPassword.matches((("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_]).{6,}")))) {
			System.out.println("Invalid password condition. Set again :");
			userPassword = sc.next();
		}
		System.out.println("Confirm Your password by Reentering it");
		String ConfirmPassword=sc.next();
		//password and confirm pass validation
		while(!userPassword.equals(ConfirmPassword))
		{
			System.out.println("Invalid password, Sholud match with your password");
			ConfirmPassword=sc.next();
		}
		System.out.println("Please Enter your PHONE NUMBER");
		String phoneNo=sc.next();
		System.out.println("Please Enter your ADDRESS");
		String address=sc.next();
		
		System.out.println("Please Enter Your BALANCE");
		int balance=sc.nextInt();
		while(balance<500)
		{
			//inital balance>500
			logger.info("Initial balance should be >500 ,deposit again");
			balance=sc.nextInt();
			
			if(balance<500){
				throw new MinimumAmountException("Minimum balance to create Your account is 500");
			}
		}
		
		//if initial balance is >500 account is allowed to procceed
			System.out.println("Please wait for confimation...");
			System.out.println("Accepted.. u can proceed");
		
		
		customer=new Customer(accountNo, userName, userPassword, address, phoneNo, balance);
		return customer;
	}
	
		
}
