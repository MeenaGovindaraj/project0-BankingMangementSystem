package com.revature.pms.model;


/*
 * create table employee(
	employeeId int primary key,
	employeeName varchar(50) not null,
	employeepassword varchar(30) not null,
	phoneno varchar(10),
	address varchar(40)
)
*/
public class Employee {

	//Id,name,pass,phno,address
	private String employeeName,employeePassword,address,phoneNo;
	private int employeeId;
	
	public Employee()
	{
		
	}

	public Employee(String employeeName, String employeePassword, String address, String phoneNo, int employeeId) {
		super();
		this.employeeName = employeeName;
		this.employeePassword = employeePassword;
		this.address = address;
		this.phoneNo = phoneNo;
		this.employeeId = employeeId;
	}

	public String getemployeeName() {
		return employeeName;
	}

	public void setemployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getemployeePassword() {
		return employeePassword;
	}

	public void setemployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
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

	public int getemployeeId() {
		return employeeId;
	}

	public void setemployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeName=" + employeeName + ", employeePassword="
				+ employeePassword + ", address=" + address + ", phoneNo=" + phoneNo + "]";
	}

	
	
	
}
