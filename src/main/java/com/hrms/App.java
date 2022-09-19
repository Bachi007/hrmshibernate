package com.hrms;

import java.util.Scanner;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Hello world!
 *
 */
public class App 
{
	//config is instance variable any method can access this
	Configuration conf=null;
    public static void main( String[] args )
    {
    	//scanner object to read values from user
    	Scanner bs=new Scanner(System.in);
    	System.out.println("----HRMS----");
    	System.out.println("select Operation: \n1 for register \n2 for login");
    	int op=bs.nextInt();
    	 App ap=new App();
    	switch(op) {
    	
    	case 1->{
    		System.out.println("Employee Id");
        	int eid=bs.nextInt();
        	System.out.println("Employee Name");
        	String eName=bs.next();
        	System.out.println("Employee Password");
        	String epwd=bs.next();
        	System.out.println("Employee Domain");
        	String edomain=bs.next();
        	System.out.println("Employee Salary");
        	int esal=bs.nextInt();
        	//creating employee object to store
        	employee e1=new employee();
        	e1.setEmpName(eName);
        	e1.setEmpDomain(edomain);
        	e1.setEmpPassword(epwd);
        	e1.setEmpId(eid);
        	e1.setEmpSalary(esal);
        	ap.connection();
        	//registration
        	ap.register(e1);
    		}
    	
    	case 2->{
    		//login
    		System.out.println("Enter details for login");
    		System.out.println("Enter Username");
    		String username=bs.next();
    		System.out.println("Enter Password");
    		String password=bs.next();
    		ap.connection();
    		//calling login
    		int res=ap.login(username, password);
    		if(res==-1) {
    			System.out.println("Username or Password is wrong");
    		}
    		else if(res==0) {
    			System.out.println("user not found");
    		}
    		else {
    			//login success
    			System.out.println("Login success");
    			System.out.println("Select Op: \n1 for print details \n2 for change domain \n3 for change password \n4 for delete account");
    			int op2=bs.nextInt();
    			//new options for user
    			switch(op2) {
    			//print details based on eid
    			case 1->{
    					employee e3=ap.printEmployee(res);
    					System.out.println(e3);
    			}
    			//change domain of the user based on eid
    			case 2->{
    					System.out.println("Enter New Domain");
    					String newdomain=bs.next();
    					//calling changedomain
    					int status=ap.changeDomain(res, newdomain);
    					if(status==1) {
    						System.out.println("New Domain Updated");
    					}
    					else {
    						System.out.println("Something went wrong");
    					}	
    				}
    			//change password old but password must be matched with current one
    			case 3->{
    					System.out.println("Enter Old Password");
    					String oldpwd=bs.next();
    					System.out.println("Enter new password");
    					String newpwd=bs.next();
    					int st=ap.changePassword(res, oldpwd, newpwd);
    					if(st==1) {
    						System.out.println("Password changed");
    					}
    					else{
    						System.out.println("something went wrong");
    					}
    					}
    			//deleting account
    			case 4->{
    				System.out.println("Are you sure to delete account(yes/no)");
    				String are=bs.next();
    				if(are.equals("yes")) {
    					int sta=ap.deleteAccount(res);
    					if(sta==1) {
    						System.out.println("Account deleted");
    					}
    					else {
    						System.out.println("something went wrong");
    					}
    					}
    				}
    		}
    	}
    }
  }
    	bs.close();//closing scanner
} 
    //configuration of connection
    public void connection () {
    	conf=new Configuration().configure().addAnnotatedClass(employee.class);    		 
    }
    //registration process
    public void register(employee e1) {
    	SessionFactory sf=conf.buildSessionFactory();
    	Session ses=sf.openSession();
    	Transaction tc=ses.beginTransaction();
    	ses.save(e1);   	
    	System.out.println(e1);
    	tc.commit();
    }
    //login method
    public int login(String username,String password) {
    	SessionFactory sf=conf.buildSessionFactory();
    	Session ses=sf.openSession();
    	Transaction tc=ses.beginTransaction();
    	employee e2=null;
    	e2=(employee)ses.createQuery("from employee e where e.empName=:username").setParameter("username", username).uniqueResult();
    	if(e2==null)
    	{
    		return 0;
    	}
    	else 
    	{//entered password must be equal to actual password
    		if(e2.getEmpPassword().equals(password)) 
    		{
    		return e2.getEmpId();	
    		}
    	else 
    	{
    		return -1;
    	}
    }
 }
    	//returning employee details
    	public employee printEmployee(int eid) {
    		SessionFactory sf=conf.buildSessionFactory();
        	Session ses=sf.openSession();
        	Transaction tc=ses.beginTransaction();
    		employee e2=null;
    		e2=(employee)ses.get(employee.class, eid);
    		return e2;
    	}
    	//updating domain of the user
    	public int changeDomain(int eid,String newDomain) {
    		SessionFactory sf=conf.buildSessionFactory();
        	Session ses=sf.openSession();
        	Transaction tc=ses.beginTransaction();
    		Query qu=ses.createQuery("update employee set empDomain=:newDomain where empId=:eid").setParameter("newDomain", newDomain).setParameter("eid", eid);
    		int status=qu.executeUpdate();
    		return status;
    	}
    	//changing of password
    	public int changePassword(int eid,String oldpwd,String newpwd) {
    		
    		SessionFactory sf=conf.buildSessionFactory();
        	Session ses=sf.openSession();
        	Transaction tc=ses.beginTransaction();
        	employee e2=ses.get(employee.class,eid);
    		if(e2.getEmpPassword().equals(oldpwd)) {	
    	Query qu=ses.createQuery("update employee set empPassword=:newpwd where empId=:empid").setParameter("newpwd", newpwd).setParameter("empid", eid);		
    			int st=qu.executeUpdate();
    			return st;
    		}
    		else {
    			return -1;
    		}
    	}
    	//deleting the account
    	public int deleteAccount(int eid) {
    		SessionFactory sf=conf.buildSessionFactory();
        	Session ses=sf.openSession();
        	Transaction tc=ses.beginTransaction();
    		Query qu=ses.createQuery("delete from employee where empId=:eid").setParameter("eid", eid);
    		int sta=qu.executeUpdate();
    		return sta;
    	}
    	
}
    
    
    
