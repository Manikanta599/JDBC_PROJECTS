package org.bankApp;

import java.util.*;
import java.sql.*;
public class User {
	
	private Connection connection;
	private Scanner sc;
	public String nam;
	
	public User(Connection connection,Scanner sc)
	{
		this.connection=connection;
		this.sc=sc;
	}
	
	public void register()
	{
		sc.nextLine();
		System.out.println("Enter Full Name: ");
		String full_name=sc.nextLine();
		System.out.println("Enter Email: ");
		String email=sc.nextLine();
		System.out.println("Enter password");
		String password=sc.next();
		
		if(user_exits(email))
		{
			System.out.println("user Already Exists for this email Address!!");
			return;
		}
		
		String query="INSERT INTO user(full_name,email,password) values(?,?,?)";
		
		try
		{
			PreparedStatement pst=connection.prepareStatement(query);
			
			pst.setString(1, full_name);
			pst.setString(2, email);
			pst.setString(3, password);
			
			int rows_affected=pst.executeUpdate();
			
			if(rows_affected>0)
			{
				System.out.println("Registration Sucessfull");
			}
			else
			{
				System.out.println("Registration failed");
			}
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	public boolean user_exits(String email)
	{
		String query="SELECT * FROM user WHERE email=?";
		
		try
		{
			PreparedStatement pst=connection.prepareStatement(query);
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
	
	public String login()
	{
		sc.nextLine();
		System.out.print("Enter Email: ");
		String email=sc.nextLine();
		System.out.print("Enter Password ");
		String pass=sc.next();
		
		String query="SELECT * FROM user WHERE email=? AND password=?";
		
		try
		{
			PreparedStatement pst=connection.prepareStatement(query);
			pst.setString(1, email);
			pst.setString(2, pass);
			
			ResultSet rs=pst.executeQuery();
			
			if(rs.next())
			{
				nam=rs.getString("full_name");
				return email;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e){
            e.printStackTrace();
        }
		return null;
	}
	

}
