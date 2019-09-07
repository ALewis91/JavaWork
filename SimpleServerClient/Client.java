import java.net.*;
import java.util.Scanner;
import java.io.*;

class Client
{
    public static void main(String[] args)
    {   	
        try
        {
        	// Init variables
        	double totalTime = 0;
        	int numTests = 100;
            String strData;
        	byte[] data = null;
        	int nsPerSec = 1000000000;
        	
        	// Loop for number of tests
        	for (int x = 0; x < numTests; x++)
        	{
        		// Create a new socket to connect to IP given as cmd line arg and port 3500
		        Socket s = new Socket(args[0], 3500);//connect to ServerSocket
		        
		        // Increase the buffer size to accomodate larger files
		        s.setSendBufferSize(100000000);
		        s.setReceiveBufferSize(100000000);
		        
		        // Remove socket timeout so it does not time out with larger files
		        s.setSoTimeout(0);
		        
		        // Create input stream for socket
		        InputStream is = s.getInputStream();  //generate InputStream from Socket
		        
		        // Create scanner to read file given as second cmd line arg
		        Scanner fileScan = new Scanner(new File(args[1]));
		        
		        // Initialize file read buffer string to empty string
		        strData = "";
		        
		        // Read file into buffer
	            while (fileScan.hasNext())
	            	strData += fileScan.next();
	            
	            // Convert file data into byte array
		        data = strData.getBytes();
		        
		        // Create output stream for the socket
		        OutputStream os = s.getOutputStream();
		        
		        // Start timer
		        long start = System.nanoTime();
		        
		        // Write the byte array to the outputstream
			    os.write(data);
			    os.flush();
			    
			    // Read the data back from the server
			    is.read(data);
			    
			    // Stop the timer
			    long stop = System.nanoTime();
			    
			    // Calculate transfer time and add it to total transfer time
			    totalTime += (stop - start);
			    
			    // Close streams and socket
			    is.close();
		        s.close();
		        fileScan.close();
        	}
        	
        	// Create a printwriter to write data from server to file
            PrintWriter pw = new PrintWriter(new File(args[2]));
            
            // Convert byte array back to string
            strData = new String(data);
            
            // Write the data from the last transfer to the file
            pw.write(strData);
            
            // Close the stream
            pw.close();
            
            // Calculate the average transfer time in seconds
        	double avgTime = totalTime/numTests/nsPerSec;
        	
        	// Print average transfer time
        	System.out.println("Time to send file to and receive file from server: " + avgTime + " seconds");

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
