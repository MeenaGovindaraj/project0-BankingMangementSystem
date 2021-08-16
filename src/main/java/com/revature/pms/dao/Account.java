package com.revature.pms.dao;

import java.util.List;

import com.revature.pms.exceptions.UserException;
import com.revature.pms.model.Customer;

public interface Account {

	public boolean createAccount(Customer customer);
	public boolean updateAccount(Customer customer);
	public boolean deleteAccount(int AccountNo);
	public List getAccountById(int AccoutNo);
	public Customer validateAccount(int accountNo,String userPassword) throws UserException;
}
