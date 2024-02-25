package org.bankApp;
import java.sql.*;
import java.util.*;

public class Accounts {
	private Connection con;
	private Scanner sc;
	
	public Accounts(Connection con,Scanner sc)
	{
		this.con=con;
		this.sc=sc;
	}
	
	
	public long open_account(String email,String name)
	{
		if(!account_exists(email))
		{
			String query="INSERT INTO accounts(account_number,full_name,email,balance,security_pin) values (?,?,?,?,?)";
			sc.nextLine();
			System.out.println("Enter Initial Amount");
			double bal=sc.nextDouble();
			System.out.println("Enter Security Pin");
			String sec_pin=sc.next();
			
			try
			{ 
				long acc_num=generateAccNumber();
				
				PreparedStatement pst=con.prepareStatement(query);
				pst.setLong(1, acc_num);
				pst.setString(2, name);
				pst.setString(3, email);
				pst.setDouble(4, bal);
				pst.setString(5, sec_pin);
				
				int n=pst.executeUpdate();
				
				if(n>0)
				{
					return acc_num;
				}
				}
			
			catch (SQLException e) {
                e.printStackTrace();
            }

			}
		throw new RuntimeException("Account Creation failed!!");

		}
	
	
	public boolean account_exists(String email)
	{
		String query="SELECT account_number FROM accounts WHERE email=?";
		
		try
		{
			PreparedStatement pst=con.prepareStatement(query);
			pst.setString(1, email);
			
			ResultSet rs=pst.executeQuery();
			
			if(rs.next())
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		catch (SQLException e){
            e.printStackTrace();
        }
		return false;
	}
	
	private long generateAccNumber()
	{
		try
		{
			Statement st=con.createStatement();
			String query="SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
			ResultSet rs=st.executeQuery(query);
			
			if(rs.next())
			{
				long last_acc_num=rs.getLong("account_number");
				return last_acc_num+1;
			}
			else
			{
				return 1000010000;
			}
		}
		catch (SQLException e){
            e.printStackTrace();
        }
		return 1000010000;
	}
	
	
	
	public long getAccount_num(String email)
	{
		String query="SELECT account_number FROM accounts WHERE email=?";
		try
		{
			PreparedStatement pst=con.prepareStatement(query);
			pst.setString(1, email);
			
			ResultSet rs=pst.executeQuery();
			if(rs.next())
			{
				return rs.getLong("account_number");
			}
		}
		catch (SQLException e){
            e.printStackTrace();
        }
		
		throw new RuntimeException("Account Number Doesn't Exist!");
	}
	
	
	}
	


