package com;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.pms.dao.*;
import com.revature.pms.model.Employee;

public class App 
{
	static String name;
	
    public static void main( String[] args )
    {
    	Logger logger=Logger.getLogger("App");
    	logger.info("\n\n############### Banking Mangement APP #############");
       
       Scanner sc=new Scanner(System.in);
       System.out.println("\nPlease Enter Your name...");
       name =sc.nextLine();
       
       // to start the app based on users
       startApp();
       
    }
    
    public static void startApp ()throws InputMismatchException
    {
  
    	
    	Logger logger=Logger.getLogger("App");
    	Scanner sc=new Scanner(System.in);
    	logger.info("\n---------------------Welcome,"+name+"! Have a Nice Day :)--------------");
    	
    	try{
	    	
	    	while(true){
	    		
	     	   int choice=0;
 	       System.out.println("\n1. Create Account");
 	       System.out.println("2. Login");
 	       System.out.println("3. Know more about banking app");
 	       System.out.println("4. Know about developers ");
 	       System.out.println("5. Exit ");
	       
 	      System.out.println("Enter Your Choice");
 	       choice=sc.nextInt();
 	       switch(choice)
 	       {
 	       case 1:// to create account of any type(E/C)
 	    	   
 	    	   logger.info("Enter your account type to create( E for Employee / C for Customer )");
 	    	   String type=sc.next();
 	 
 	    	   if(type.equals("C")){
 	    		   
	 	    	   CustomerApp customer= new CustomerApp();
	 	    	   customer.startCustomer();
 	    	   }
 	    	   else if(type.equals("E"))
 	    	   {
 	    		  EmployeeApp employee=new EmployeeApp();
 	 	    	  employee.startEmployee();
 	    	   }
 	    	   else
 	    	   {
 	    		   logger.error("Invalid Type : "+type);
 	    	   }
 	    	   break;
 	    	   
 	       case 2://to Login account of any type(E/C)
 	    	 
 	    	  logger.info("Enter your account type to Login( E for Employee / C for Customer )");
	    	   type=sc.next();
	    	   if(type.equals("C")){
	    		   
	    		   CustomerApp customer= new CustomerApp();
	 	    	   customer.startCustomerLogin();
	    	   }
	    	   else if(type.equals("E"))
	    	   {
	    		   EmployeeApp employee=new EmployeeApp();
	    		   employee.startEmlpoyeeLogin();;
	    	   }
	    	   else
 	    	   {
	    		   logger.error("Invalid Type : "+type);
 	    	   }
	    	   break;
	    	   
 	       case 3://to know more about bank
 	    	  
 	    	   System.out.println("\n Here u can perform banking operation as mention by developer");
 	    	   break;
 	    	   
 	       case 4://to Know more about developers
 	    	  
 	    	   System.out.println("\n  Hey Hi ! This is Meena. \n Thanks for visting my Application \n In this application u can create a account for employee as well as customer...\n Customer and employee can update there accounts.\n Customer can perform certain operations like Withdraw,Deposit,TranferFund...\n Employee can view customers log of transcation also" );
 	    	   break;
 	    	   
 	       case 5:
 	    	   logger.info("Thanks for using this APP :)");
 	    	   System.exit(0);
 	    	   
 	    	   default:
 	    		  logger.error("Invalid Option : "+choice);
 	    		   break;
 	       }
 	       
        }
    }
    //wrong input type
    catch(InputMismatchException e1)
	{
		System.out.println("Input Mismatch!!- Enter Correct Input format");
		startApp();
	}
    }
}

