import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;

import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;

class event extends JFrame{

	//The value entered in GUI are stored here. and called by the client.
	String id = null; 
	String password = null;
	JTextArea text_display;
	
		//Login page.
		event(){
			
			setTitle("LOGIN");
			
			JPanel panel = new JPanel();
			panel.setLayout(null);
			
			JLabel label_id = new JLabel("id:"); //Id label.
			JLabel label_password = new JLabel("password:"); //Password label.
			JTextField text_id = new JTextField(); //Id text field.
			JTextField text_password = new JTextField(); //Password text field.
			JButton button_login = new JButton("LOGIN"); //Login button.
			JButton button_join = new JButton("JOIN"); //Join button.
			
			//Locate the components.
			label_id.setBounds(20,50,80,30); text_id.setBounds(120,50,100,30);
			label_password.setBounds(20,100,80,30); text_password.setBounds(120,100,100,30);
			button_login.setBounds(70,200,100,30); button_join.setBounds(180,200,100,30);

			//Attach the components to the panel.
			panel.add(label_id);panel.add(text_id);
			panel.add(label_password);panel.add(text_password);
			panel.add(button_login);panel.add(button_join);
			
			//Attach the panel to the frame.
			add(panel);
			
			//Event handler activated when the button on this frame is pressed.
			ActionListener l=new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if(e.getSource()==button_login) {
						id = text_id.getText();
						password = text_password.getText();
					}
					
					//if 'join button' is pushed, launch a web page for user registration. 
					if(e.getSource()==button_join) {
						String url="http://davichiar1.cafe24.com/rg.html";
						try { Desktop.getDesktop().browse(new java.net.URI(url)); } 
						catch (IOException x) { x.printStackTrace(); } 
						catch (URISyntaxException x) { x.printStackTrace(); } 
						} 
					}
			};

			button_login.addActionListener(l); 
			button_join.addActionListener(l);
			
			setSize(400,300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			
		}
	
	//Chat room page.
	event(PrintWriter out,String port,Connection con,String name) throws SQLException{
		
		setTitle(name);

		JPanel panel_send=new JPanel();
		
		JLabel label_send=new JLabel("message to send");
		JTextField text_send = new JTextField(10);
		JButton button_send = new JButton("SEND");
		
		panel_send.add(label_send);
		panel_send.add(text_send);
		panel_send.add(button_send);
		
		//Create chat window component.
		text_display = new JTextArea();
		text_display.setEditable(false);
		text_display.setLineWrap(true);
		text_display.setForeground(Color.BLUE);
		
		//Create scroll bar.
		JScrollPane jsp=new JScrollPane(text_display);
		JScrollBar jsb=new JScrollBar();
		jsb=jsp.getVerticalScrollBar();
	
		add(jsp,BorderLayout.CENTER);
		add(panel_send,BorderLayout.SOUTH);
		
		//Event handler that operates when the send button is pressed.
		ActionListener l=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Get message from text field.
				String msg=text_send.getText();
				
			    PreparedStatement pstmt=null;
			    
			    //Get current time to send information to the database.
				String SQL="SELECT NOW()";
				ResultSet rs=null;
			    try {pstmt = con.prepareStatement(SQL);} 
			    catch (SQLException e3) {e3.printStackTrace();}
			    try {rs = pstmt.executeQuery(); rs.next();} 
				catch (SQLException e3) {e3.printStackTrace();}
				
			    //Send chat information to database.
				StringBuilder sql=new StringBuilder();
				sql.append("INSERT INTO chatting_log (port, name, content, time)");
			    sql.append("VALUES (?, ?, ?, ?)");
				
			    try {pstmt=con.prepareStatement(sql.toString());} 
				catch (SQLException e3) {e3.printStackTrace();}
				
				try {pstmt.setString(1,port);} 
				catch (SQLException e1) {e1.printStackTrace();}
				
				try {pstmt.setString(2,name);} 
				catch (SQLException e1) {e1.printStackTrace();}
				
				try {pstmt.setString(3, msg);} 
				catch (SQLException e1) {e1.printStackTrace();}
				
				try {pstmt.setString(4, rs.getString(1));} 
				catch (SQLException e2) {e2.printStackTrace();}
				
				try {pstmt.execute();} 
				catch (SQLException e1) {e1.printStackTrace();}	
				
				//Send message to server while outputs it to the client's chat window. 
				calendar time=new calendar();
				String log=text_display.getText();
				if(log.length()==0) text_display.setText(name+": "+msg+" "+time);
				else text_display.setText(log+"\n"+name+": "+msg+" "+time);
				out.println(msg);
			}
		};
		
		button_send.addActionListener(l);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,400);
		setVisible(true);
	}
}
