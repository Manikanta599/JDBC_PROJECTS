package org.bankApp;
import java.sql.*;
import java.util.*;



public class AccountManager {
	private Connection con;
	private Scanner sc;
	
	public AccountManager(Connection con,Scanner sc)
	{
		this.con=con;
		this.sc=sc;
	}
	
	public void credit_money(long acc_num) throws SQLException
	{
		sc.nextLine();
		System.out.println("Enter Amount");
		double amount=sc.nextDouble();
		System.out.println("Enter Security pin");
		String pin=sc.next();
		
		try
		{
			con.setAutoCommit(false);
			
			if(acc_num!=0)
			{
				String query="SELECT * FROM accounts WHERE account_number=? AND security_pin=?";
				PreparedStatement pst=con.prepareStatement(query);
				pst.setLong(1, acc_num);
				pst.setString(2, pin);
				
				ResultSet rs =pst.executeQuery();
				
				if(rs.next())
				{
					String credit_query="UPDATE accounts SET balance=balance+ ? WHERE account_number=?";
					PreparedStatement pst1=con.prepareStatement(credit_query);

					pst1.setDouble(1, amount);
					pst1.setLong(2, acc_num);
					
					int r=pst1.executeUpdate();
					
					if(r>0)
					{
						System.out.println("Rs. "+amount+" credited Successfully!!");
						con.commit();
						con.setAutoCommit(true);
						return;
					}
					else
					{
						System.out.println("Transaction Failed!");
						con.rollback();
						con.setAutoCommit(true);
						
					}
				}
				else
				{
					System.out.println("Invalid Security Pin!!");
				}

			}
		}
		catch (SQLException e){
            e.printStackTrace();
        }
		con.setAutoCommit(true);
	}
	
	
	public void debit_money(long acc_num) throws SQLException
	{
		//sc.next();
		System.out.println("Enter Amount");
		double amount=sc.nextDouble();
		
		System.out.println("Enter Security pin");
		String pin=sc.next();
		
		try
		{
			con.setAutoCommit(false);
			if(acc_num!=0)
			{
				String debit_query="SELECT * FROM accounts WHERE account_number=? AND security_pin=?";
				PreparedStatement pst=con.prepareStatement(debit_query);
				pst.setLong(1, acc_num);
				pst.setString(2, pin);
				
				ResultSet rs=pst.executeQuery();
				
				if(rs.next())
				{
					double cur_bal=rs.getDouble("balance");
					if(amount<cur_bal)
					{
						String d_query="UPDATE accounts SET balance=balance-? WHERE account_number=?";
						PreparedStatement pst2=con.prepareStatement(d_query);

						pst2.setDouble(1, amount);
						pst2.setLong(2, acc_num);
						
						int r=pst2.executeUpdate();
						if(r>0)
						{
							System.out.println("Rs. "+amount+" debited Successfully");
							con.commit();
							con.setAutoCommit(true);
							return;
						}
						else
						{
							System.out.println("Transaction failed!!");
							con.rollback();
							con.setAutoCommit(true);
						}
					}
					
					else
					{
						System.out.println("Insufficient Balance!");
					}
				}
				else
				{
					System.out.println("Invalid Pin!");
				}
				

			}
		}
		catch (SQLException e){
            e.printStackTrace();
        }
		con.setAutoCommit(true);
	}
	
	public void checkBalance(long acc_num)
	{
		//sc.next();
		System.out.println("Enter Security Pin:");
		String pin=sc.next();
		
		try
		{
			String query="SELECT balance FROM accounts WHERE account_number=? AND security_pin=?";
			PreparedStatement pst=con.prepareStatement(query);
			
			pst.setLong(1, acc_num);
			pst.setString(2, pin);
			
			ResultSet rs=pst.executeQuery();
			
			if(rs.next())
			{
				double bal=rs.getDouble("balance");
				
				System.out.println("Balance "+bal);
			}
			else
			{
				System.out.println("Invalid pin!");
			}
		}
		catch (SQLException e){
            e.printStackTrace();
        }
	}
	
	public void trasfer_money(long acc_num) throws SQLException
	{
		System.out.println("Enter Benificiary account numner: ");
		long ac_no=sc.nextLong();
		if(acc_num!=ac_no)
		{
		System.out.println("Enter amount: ");
		double amount=sc.nextDouble();
		System.out.println("Enter Your Pin:");
		String pin=sc.next();
		
		
		
		con.setAutoCommit(false);
		
		String query="SELECT * FROM accounts WHERE account_number=? AND security_pin=?";
		PreparedStatement pst2=con.prepareStatement(query);
		pst2.setLong(1, acc_num);
		pst2.setString(2, pin);
		
		ResultSet rs=pst2.executeQuery();
		
		if(rs.next())
		{
			double av_amount=rs.getDouble("balance");
			String que="SELECT * FROM accounts WHERE account_number=?";
			PreparedStatement pst3=con.prepareStatement(que);
			pst3.setLong(1, ac_no);
			
			ResultSet rs1=pst3.executeQuery();
			if(rs1.next())
			{
				if(amount<av_amount)
				{
					String credit="UPDATE accounts SET balance=balance+? WHERE account_number=?";
					String debit="UPDATE accounts SET balance=balance-? WHERE account_number=?";
					
					PreparedStatement ps1=con.prepareStatement(credit);
					PreparedStatement ps2=con.prepareStatement(debit);
					
					ps1.setDouble(1, amount);
					ps1.setLong(2, ac_no);
					
					ps2.setDouble(1, amount);
					ps2.setLong(2,acc_num);
					
					int c=ps1.executeUpdate();
					int d=ps2.executeUpdate();
					
					if(c>0&&d>0)
					{
						System.out.println("Transfer Sucessfull");
						con.commit();
						con.setAutoCommit(true);
						return;
					}
					else
					{
						System.out.println("Transfer failed");
						con.rollback();
						con.setAutoCommit(true);
						return;
					}
					
				}
				else
				{
					System.out.println("Insufficient balance!!");
				}
			}
			else
			{
				System.out.println("Invalid account number!!");
			}
			
			
		}
		else
		{
			System.out.println("Invalid Pin!!");
		}
		

		//System.out.println("tranfer amount");
	}
		
		else
		{
			System.out.println("Please Enter Benificiary Account Number Not Yours!!");
		}
	}
	
	public void changePin(long acc_num)
	{
		System.out.println("Enter Your Current Pin");
		String c_pin=sc.next();
		
		String query="SELECT * FROM accounts WHERE security_pin=?";
		try {
			PreparedStatement pst=con.prepareStatement(query);
			pst.setString(1, c_pin);
			ResultSet rs=pst.executeQuery();
			
			if(rs.next())
			{
				System.out.println("Enter new Pin");
				String n1=sc.next();
				System.out.println("Enter Again new Pin");
				String n2=sc.next();
				
				
				if(n1.equals(n2))
				{
					String uquery="UPDATE accounts SET security_pin=? WHERE account_number=?";
					PreparedStatement pst1=con.prepareStatement(uquery);
					pst1.setString(1, n2);
					pst1.setLong(2, acc_num);
					
					int n=pst1.executeUpdate();
					
					if(n>0)
					{
						System.out.println("Pin Changed Successfull!!");
					}
					else
					{
						System.out.println("Pin Changed Not Successfull!!");
					}
					
				}
				else
				{
					System.out.println("Pin not matched!!");
				}
				
			}
			else
			{
				System.out.println("Invalid Pin!!");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void updateEmail(long acc_num) throws SQLException
	{
		System.out.println("Enter Your Pin");
		String pin=sc.next();
		
		String query="SELECT * FROM accounts WHERE security_pin=?";
		
		PreparedStatement pst=con.prepareStatement(query);
		
		pst.setString(1, pin);
		ResultSet rs=pst.executeQuery();
		
		if(rs.next())
		{
			System.out.println("Enter 1 to see the Current Email Else Enter 0:");
			int c=sc.nextInt();
			
			if(c==1)
			{
				String query1="SELECT email FROM accounts WHERE security_pin=?";
				
				PreparedStatement pst1=con.prepareStatement(query1);
				
				pst1.setString(1, query);
				ResultSet rs1=pst1.executeQuery();
				
				String e=rs1.getString("email");
				
				System.out.println(e);
				c=0;
			}
			
			if(c==0)
			{
				System.out.println("Enter new Email");
				String ne=sc.nextLine();
				
				String u_query="UPDATE accounts SET email=? WHERE account_number=?";
				PreparedStatement pst2=con.prepareStatement(u_query);
				
				pst2.setString(1, ne);
				pst2.setLong(2, acc_num);
				
				int n=pst2.executeUpdate();
				
				if(n>0)
				{
					System.out.println("email Updated Successfully");
				}
				else
				{
					System.out.println("email Not Updated!!");
				}
			}
		}
		else
		{
			System.out.println("Invalid Pin !!");
		}
	}
	
	public void trnsactionHis(long acc_num)
	{
		System.out.println("working on it..");
	}

}
