package orderingSystem;
import java.lang.*;
import java.util.*;
import java.sql.*;

public class Main {
	public static void main(String[] args) throws Exception{
		  try {
			  Class.forName("com.mysql.cj.jdbc.Driver");
			  Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantdb","root","");
			  Statement st=con.createStatement();
			  
			  Scanner sc=new Scanner(System.in);
			  
			  System.out.println("WELCOME TO OUR RESTAURANT");
			  System.out.println("Do you want the Menu?(press m)");
			  char menu =sc.next().charAt(0);
			  if(menu == 'M' || menu=='m') {
			  ResultSet menuDisplay=st.executeQuery("select * from Menu");
			  System.out.println("Here is the Menu!");
			  System.out.printf("%-15s %-20s %-15s","Food_id","Food_name","Price");
			  System.out.println();
			  while(menuDisplay.next()) {
				  System.out.printf("%-15s %-20s %-15s",menuDisplay.getInt(1),menuDisplay.getString(2),menuDisplay.getInt(3));
				  System.out.println();
			  }
			  }
			  System.out.println("To place your order, give us the food ids from the menu seperated with space");
			  sc.nextLine();
			  String orderInput=sc.nextLine();
			  String[] orderTemp=orderInput.split(" ");
			  int[] order=new int[orderTemp.length];
			  
			  for(int i=0;i<order.length;i++) {
				  order[i]=Integer.parseInt(orderTemp[i]);
			  }
			  
			  System.out.println("Order Taken");
			  System.out.println("Here's your order:");
			  System.out.println();
		
			  
			  Statement createst = con.createStatement();
			  createst.executeUpdate("CREATE TABLE ordertemp(Food_id INT PRIMARY KEY,Food_name VARCHAR(255),Price DECIMAL(10, 2));");
			  
			  for(int i=0;i<order.length;i++) {
				  PreparedStatement psc=con.prepareStatement("insert into ordertemp select * from Menu where Food_id=? order by Food_id");
				  psc.setInt(1, order[i]);
				  psc.executeUpdate();
				  psc.close();
			  }
			  
			  Statement selectst = con.createStatement();
			  ResultSet orderDisplay = selectst.executeQuery("SELECT * FROM ordertemp");
			  
			  int billAmount=0;
			  System.out.printf("%-15s %-20s %-15s\n","Food_id","Food_name","Price");
			  while(orderDisplay.next()) {
				  System.out.printf("%-15s %-20s %-15s",orderDisplay.getInt(1),orderDisplay.getString(2),orderDisplay.getInt(3));
				  System.out.println();
				  billAmount+=orderDisplay.getInt(3);
			  }
			  System.out.println();
			  System.out.printf("%-15s %-20s %-15s\n\n","Total Amount","",billAmount);
			  System.out.println("Your bill amount will be Rs."+billAmount);
			  
			  Statement deletest = con.createStatement();
			  deletest.executeUpdate("drop table ordertemp");
			  
			  System.out.println("Press to 'c' to confirm your order");
			  
			  char choice=sc.next().charAt(0);;
			  
			  if(choice=='c'||choice=='C') {
			  System.out.println("YOUR ORDER IS COMING RIGHT AWAY");
			  System.out.println("ENJOY YOUR FOOD.HAVE A GREAT DAY!");
			  }
			  else
			   System.out.println("THANK YOU. If you want to place another order, kindly run the system again");
			  
			  sc.close();
			  con.close();
		  }
		  catch(Exception e) {
			  System.out.println(e);
		  }
		  
	  }
}
