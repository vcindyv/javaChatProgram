import java.text.SimpleDateFormat;
import java.util.Date;

class calendar{
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //Set format.
	Date currentTime = new Date(); //Get current time.
	String ctime = formatter.format(currentTime); //Convert current time to this format and save.
	
	public String toString() {
		return "["+ctime+".0"+"]";
	}
}