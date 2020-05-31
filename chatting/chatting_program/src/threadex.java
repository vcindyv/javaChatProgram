import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class threadex extends Thread{
   
   Socket client_socket;
   int i;
   calendar current_time;
   //Receive socket associated with client and client's index from server.
   public threadex(Socket client_socket,int i) {
	   this.client_socket=client_socket;
	   this.i=i;
	   }
   
   public void run() {
	  
	  String name=null;
	  
	  try {
		  
      //Create an input stream for the client.
	  BufferedReader in = null;
      try {
         in=new BufferedReader(new InputStreamReader
               (client_socket.getInputStream()));} 
      catch (IOException e) {System.out.println("failed to create an input stream!");e.printStackTrace();}
      
      //Receive client's name.
      while(name==null) {
      
    	  try {name=in.readLine();} 
    	  catch (IOException e1) {System.out.println("failed to receive client's name");e1.printStackTrace();}
      
      }
      current_time=new calendar();
      System.out.println("["+name+"]"+" "+"is online"+" "+current_time);
	  
      //Receive messages from the client related to this thread and send it to all clients.
      String msg = null;
      while(in!=null) {
    	    //Receive messages.
            msg=in.readLine(); 
            current_time=new calendar();
            System.out.println(name+": "+msg+" "+current_time);
            //Send messages to all clients.
            for(int j=0;j<server.i;j++) {
               if(j==i) continue;
               server.out[j].println(name+": "+msg+" "+current_time);
            }
            
      }
      
      }
	  
	  //Things to do when threads end. 
	  catch(IOException e) { 
		  
		current_time=new calendar();
		System.out.println("["+name+"]"+" "+"is offline"+" "+current_time);
		
		//Close the socket associated with the client.
		try {client_socket.close();} 
		catch (IOException e1) {e1.printStackTrace();}
		try {server.client_socket[server.i].close();} 
		catch (IOException e1) {e1.printStackTrace();}
		
	   }
	  
   }
   
}