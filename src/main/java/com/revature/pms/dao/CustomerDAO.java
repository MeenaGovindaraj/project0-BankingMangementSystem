package com.revature.pms.dao;

import java.util.List;

import com.revature.pms.exceptions.InsufficientBalance;
import com.revature.pms.exceptions.MinimumAmountException;
import com.revature.pms.exceptions.UserException;
import com.revature.pms.model.Customer;

public interface CustomerDAO {

	public boolean createAccount(Customer customer);
	public boolean updateAccount(Customer customer);
	public boolean deleteAccount(int AccountNo);
	public List getAccountById(int AccoutNo);
	public Customer validateAccount(int accountNo,String userPassword) throws UserException;
	public  boolean isAccountExists(int accountNo);
	public int viewBalance(int accountNo);
	public void transferAmount(int senderAccountNo, int recieverAccountNo, int amount) throws InsufficientBalance;
	public void withdraw(int accountNo, int amount) throws InsufficientBalance, MinimumAmountException;
	public void deposit(int accountNo, int amount);
	public void viewStatement(int accountno);
}
