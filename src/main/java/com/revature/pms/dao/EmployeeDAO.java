package com.revature.pms.dao;

import java.util.List;

import com.revature.pms.exceptions.UserException;
import com.revature.pms.model.Customer;
import com.revature.pms.model.Employee;

public interface EmployeeDAO {

		//methods for employee operations
		public boolean createAccount(Employee employee);
		public boolean updateAccount(Employee employee);
		public boolean deleteAccount(int AccountNo);
		public List getAccountById(int AccoutNo);
		public Employee validateAccount(int accountNo,String userPassword) throws UserException;
		public void viewAllLog();
		public boolean isEmployeeExists(int employeeId);
		public void viewCustomerLogById(int accountno);
		public List<Customer> viewAllCustomers();
}
