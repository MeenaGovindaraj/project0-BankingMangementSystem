package com;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.pms.dao.CustomerDAO;
import com.revature.pms.dao.CustomerDAOImpl;
import com.revature.pms.exceptions.UserException;
import com.revature.pms.model.Customer;
import com.revature.pms.model.Employee;
import com.revature.pms.dao.*;
import com.revature.pms.util.*;
public class EmployeeApp {

	Logger logger=Logger.getLogger("EmployeeApp");
	Employee employee=null;
	EmployeeDAO employeeDAO=new EmployeeDAOImpl();
	EmployeeDAOImpl employeeDAOImpl=new EmployeeDAOImpl();
	Scanner sc=new Scanner(System.in);
	
	//for creating a employee account
	public void startEmployee() {

		boolean result=false;
		employee=acceptemployeeDetails();
		//to check account existence
		try{
		if(employeeDAOImpl.isEmployeeExists(employee.getemployeeId()))
		{
			throw new UserException("\n#Sorry, "+employee.getemployeeName()+", Account is NOT CREATED successfully!!");
		}
		//if not exists creating an account
		else
		{
			result = employeeDAO.createAccount(employee);
			System.out.println("\n#Congrats,"+ employee.getemployeeName()+", your Employee Account CREATED successfully!! on "+new Date());
		}
		}
		//for invalid input type
		catch(InputMismatchException e1)
		{
			logger.error("Input Mismatch!!- Enter Correct Input format");
		} catch (UserException e) {
			logger.error("Accout Already Exists!!\n"+ e.getMessage());
		}
	}

	
	//login 

	public void startEmlpoyeeLogin() {
		
		boolean result=false;
		//login needs employee id  and password..
		System.out.println("Enter Employee ID:");
		int accountNo=sc.nextInt();
		
		System.out.println("Enter USER PASSWORD:");
		String employeePassword=sc.next();

		try {
			employee=employeeDAO.validateAccount(accountNo,employeePassword);
			//if there is no employee found with this emp name and id
			if(employee==null){
				throw new UserException("Please TRY AGAIN! INCORRECT Name/Password");
			}
			logger.info("\n############### Login Screen ##########################");
			//if employee found- performing operations(update,delete,view customers...)
			employeeOperations(employee);
		} 
		catch (UserException e) {
			logger.error("INVALID!! "+e.getMessage());
		}
		
	}

	//for more employee operations
	private void employeeOperations(Employee employee) {
		try{
		System.out.println("\n                     Welcome , "+employee.getemployeeName());
		logger.info("\n############### Personal page for " + employee.getemployeeName()+ " ##############");
		int choice=0;
		boolean result=false;
		while(true)
		{
			System.out.println("\n1. View Account");
			System.out.println("2. Update account");
			System.out.println("3. Delete Account");
			System.out.println("4. view customer");
			System.out.println("5. view all customers");
			System.out.println("6. view log of Transaction of a customer");
			System.out.println("7. view all log of Transaction");
			System.out.println("8. Logout");
			System.out.println("9. Exit");
			choice=sc.nextInt();
			
			switch(choice)
			{
			case 1:
				
				logger.info("\n# Welcome to View Account section");
				List<Employee> employees=employeeDAO.getAccountById(employee.getemployeeId());
				if(employees.isEmpty()==true)
					System.out.println("There is No account with "+employee.getemployeeId());
				else
					System.out.println(employees);
				break;
		
			case 2:
				logger.info("\n# Welcome to Update account Section");
				//updating the account 
				employee=getUpdate(employee);
				try{
				if(employeeDAOImpl.isEmployeeExists(employee.getemployeeId())){	
					//update account(as per requirements like name,pass,address..)
					result=employeeDAO.updateAccount(employee);
				if(result==true)
					logger.info(employee.getemployeeName()+ ",Your account Updated Successfully!! on "+new Date());
				}
				else
					logger.error(employee.getemployeeId()+ " Not Updated");
				}
				catch(InputMismatchException e1)
				{
					logger.error("Input Mismatch!!- Enter Correct Input format");
					
				}
				break;
			case 3:
				logger.info("\n# Welcome to Delete Account Section");
				if(employeeDAOImpl.isEmployeeExists(employee.getemployeeId())){
					result=employeeDAO.deleteAccount(employee.getemployeeId());
				if(result){
					logger.info(employee.getemployeeId()+" , deleted Successfully!! on "+new Date());
					App app=new App();
					app.startApp();
				}
				}
				else
					logger.error(employee.getemployeeId()+" , NOT deleted Successfully!!");	
				break;
			case 4:
				logger.info("\n# Welcome to View Customer by Account Number section");
				logger.info("Enter the Account of customer that U want to view");
				int accountNo=sc.nextInt();
				
				CustomerDAO customerDAO=new CustomerDAOImpl();
				List<Customer> customer=customerDAO.getAccountById(accountNo);
				
				if(customer.isEmpty()==true)
					logger.error("There is No Customer with this account Number "+accountNo);
				else
					System.out.println(customer);
				break;
			case 5:
				logger.info("\n# Welcome to View all Customer section");
				List<Customer> customers=employeeDAOImpl.viewAllCustomers();
				System.out.println(customers);
				break;
			case 6:
				
				logger.info("\n# Welcome to View Transaction section");
				System.out.println("Enter Customer Account No");
				accountNo=sc.nextInt();
				customerDAO=new CustomerDAOImpl();
				customer=customerDAO.getAccountById(accountNo);
				
				if(customer.isEmpty()==true)
					logger.error("There is No Customer with this account Number "+accountNo);
				else
					employeeDAOImpl.viewCustomerLogById(accountNo);
				break;
			case 7:
					logger.info("\n# Welcome to View Transaction section");
					employeeDAO.viewAllLog();
					break;
			case 8:
				logger.info("\n# Logout");
				logger.info("Logouted Successfully!!");
				
				//after logout the start menu will shown
				App app=new App();
				app.startApp();
				break;
			case 9:
				logger.info("\nExcited!");
				logger.info("Thanks for Using this app");
				System.exit(0);
				break;
			  default:
	    		   System.out.println("Invalid selection!");
	    		   break;
			}
			
		}
		}
		//for invalid input type
		catch(InputMismatchException e)
		{
			System.out.println("Input Mismatch!!- Enter Correct Input format");
			employeeOperations(employee);
		}
		
	}


//updating account on particular fields
	private Employee getUpdate(Employee employee) throws InputMismatchException {
		
		int choice=0;
		
		while(true){
			System.out.println("Choose to Update");
			System.out.println("1. User Name");
			System.out.println("2. User Password");
			System.out.println("3. Phone Number");
			System.out.println("4. Address");
			choice=sc.nextInt();
			switch(choice)
			{
			case 1:
				//updating name
				System.out.println("Enter NAME to update");
				String employeeName=sc.next();
				employeeName+=sc.nextLine();
				employee.setemployeeName(employeeName);
				return employee;
				
			case 2:
				//updating password
				System.out.println("Enter PASSWORD to update");
				String employeePassword=sc.next();
				employee.setemployeePassword(employeePassword);
				return employee;
				
			case 3:
				//update number
				System.out.println("Enter PHONE NUMBER to update");
				String phoneno=sc.next();
				employee.setPhoneNo(phoneno);
				return employee;
			
			case 4:
				//updating address
				System.out.println("Enter ADDRESS to update");
				String address=sc.next();
				employee.setAddress(address);
				return employee;
			
	}
	}
	}


//for creating account getting details
	private Employee acceptemployeeDetails() throws InputMismatchException{
		
		System.out.println("Please Enter Employee ID:");
		int employeeId=sc.nextInt();
		System.out.println("Please Enter Your NAME:");
		String employeeName=sc.next();
		employeeName+=sc.nextLine();
		System.out.println("Set a password [ Minimum 6 chars with upper, lower and digts and special charcters ]");
		String employeePassword = sc.next();
		//password validation
		while (!employeePassword .matches((("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_]).{6,}")))) {
			System.out.println("Invalid password condition. Set again :");
			employeePassword = sc.next();
		}
		System.out.println("Confirm Your password by Reentering it");
	
		//password and confirm pass validation
		String ConfirmPassword=sc.next();
		while(!employeePassword.equals(ConfirmPassword))
		{
			System.out.println("Invalid password, Sholud match with your password");
			ConfirmPassword=sc.next();
		}
		System.out.println("Please Enter your PHONE NUMBER");
		String phoneNo=sc.next();
		System.out.println("Please Enter your ADDRESS");
		String address=sc.next();
		
		employee =new Employee(employeeName, employeePassword, address, phoneNo, employeeId);
		return employee;
	}
}
