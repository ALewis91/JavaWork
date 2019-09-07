import java.net.*;
import java.io.*;

class Server
{
    public static void main(String[] args)
    {
        try
        {
        	// Continuously listen for new connections
            while (true)
            {
            	//declare a new ServerSocket on port 3500
                ServerSocket ss = new ServerSocket(3500);
                
                //open the ServerSocket to receive connections
	            Socket s = ss.accept( );  			
	            
	            // Increase buffer size
	            s.setSendBufferSize(100000000);
		    	s.setReceiveBufferSize(100000000);
		    	
		    	// Create output stream to write to client
	            OutputStream os = s.getOutputStream( );  
	            
	            // Create input stream to read from client
	            InputStream is = s.getInputStream(); 

	            // Create byte buffer
	            byte[] data;
	            
	            // Create input stream status indicator variable
	            int status;
	            
	            // Loop until input stream is empty
	            do
	            {
	            	data = new byte[8192];
	            	status = is.read(data);
	            	os.write(data);
	            }
	            while (status != -1 && status == 8192);
	            
	            // Flush output stream and close it
	            os.flush();  
	            os.close();  
	            
	            // Close sockets
	            s.close( );   
	            ss.close( );
            }
        }
        catch(Exception e)
        {
             System.out.println(e);
        }
   }
}
