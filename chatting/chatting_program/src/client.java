import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class client {
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException{
		
		//Connect database.
		dbconnection dbc=new dbconnection();
	    Connection con=dbc.makeConnection();
		Statement stmt=con.createStatement();
		
		//Communication-related settings.
		Socket client=null; 
		BufferedReader in=null;
	    PrintWriter out=null;
		ResultSet db_dataset=null;
		String server_ip="192.168.181.250";
		String server_port="5000";
		//Create login page.
	    event event=new event();
	    
	    //Variables to check if id,password matches.
	    boolean id=false;
		boolean password=false;
		
		//Store the name of the matching id, password. 
	    String name=null;
	    
	    //Save values from the chatting_user table in database.
	    db_dataset=stmt.executeQuery("SELECT"+" "+"*"+" "+"from"+" "+"chatting_user"+";");
	    
	    //Check if id,password matches.
	    while(id==false||password==false) {
	       
	    	System.out.print("");
	    	if(event.id!=null) {
	        db_dataset.beforeFirst();//Reset result set.
	       
	        while(db_dataset.next()) {
	        	
	        	if(event.id.equals( db_dataset.getString(1))) {id=true;} 
	        	if(event.password.equals( db_dataset.getString(2))) {password=true;}
	        	if(id==true&&password==true) break;
	        
	        }
	       
           if(id==true&&password==true) {
        	   System.out.print("id match!");
        	   System.out.println("password match!"); 
        	   name=db_dataset.getString(3);
           }
           else if(id==true&&password==false) {
        	   System.out.print("id match!");
        	   System.out.println("password mismatch!");
           }
           else if(id==false&&password==true) {
        	   System.out.print("id mismatch!");
        	   System.out.println("password match!");
           }
           else {
        	   System.out.print("id mismatch!");
        	   System.out.println("password mismatch!");
           }
	   
	     }
	   }
	    
	    //Try to connect to the server.
	    try {
			client=new Socket();//빈 소켓 생성
			System.out.println("try to connect with a server...");
			client.connect(new InetSocketAddress(server_ip,Integer.parseInt(server_port)),3000);//서버에 연결 요청, 3000은 타임아웃 ms단위
			System.out.println("connection success!");
		} catch(Exception e) {System.out.println("connection failed!");}
	    
		//Create an I/O stream.
		in=new BufferedReader(
	   		new InputStreamReader(client.getInputStream()));//소켓의 입력스트림을 가져와 객체 생성
		out=new PrintWriter(
				client.getOutputStream(),true);//출력스트림 생성, true면 버퍼 자동 비움
			
		//Create chat window.
		event=new event(out,server_port,con,name);
		
		//Get chat logs from the database.
		db_dataset=stmt.executeQuery("SELECT"+" "+"*"+" "+"from"+" "+"chatting_log"+
		    " "+"where port="+"\'"+server_port+"\'"+";");	 
		
		String log=null;
		
		//Print out the chat logs on client's chat window.
		while(db_dataset.next()) {
		    log=event.text_display.getText();
		    if(log.length()==0) event.text_display.setText(db_dataset.getString(3)+": "+db_dataset.getString(4)+" ["+db_dataset.getString(5)+"]");
		    else event.text_display.setText(log+"\n"+db_dataset.getString(3)+": "+db_dataset.getString(4)+" ["+db_dataset.getString(5)+"]");
		 }
		event.text_display.setText(log+"\n"+"-------------------The above is a chat log from the DB------------------");
		
		//Send client's name to server.
		out.println(name);

		String msg;
		
		//Receive message from the server and print out it on chat window.
		while(in!=null) {
			    msg=in.readLine();
			    log=event.text_display.getText();
			    if(log.length()==0) event.text_display.setText(msg);
			    else event.text_display.setText(log+"\n"+msg);
		}
			
		System.out.println("shut down the client!");
		in.close();
		out.close();
		client.close();
		
	}
	
}