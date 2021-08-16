package com.revature.pms.model;


public class Customer {

	private int accountNo;
	private String userName,userpassword,address,phoneNo;
	private int balance;
	
	//default cons
	public Customer()
	{
		
	}

	//Parameterized cons
	
	public Customer(int accountNo, String userName, String userpassword, String address, String phoneNo,
			int balance) {
		super();
		this.accountNo = accountNo;
		this.userName = userName;
		this.userpassword = userpassword;
		this.address = address;
		this.phoneNo = phoneNo;
		this.balance = balance;
	}

	//getter setters

	public int getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(int accouctNo) {
		this.accountNo = accouctNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	//to print 	
	@Override
	public String toString() {
		return "\nCustomer : accouctNo=" + accountNo + ", userName=" + userName + ", userpassword=" + userpassword
				+ ", address=" + address + ", phoneNo=" + phoneNo + ", balance=" + balance + "]";
	}

	public void startCustomer() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
