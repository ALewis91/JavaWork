/*  Program:     Project 3
    Author:      Aaron Lewis
    Class:	     CSCI230 F
    Date:	     05/11/2018
    Description: Take a user-input file, create a txt
    	file to represent the compressed version 
    	created by a huffman coding tree. Take the
    	newly created "compressed" file and 
    	decompress it into a new file to show
    	compression/decompression work.

    I certify that the code below is my own work.
	
	Exception(s): N/A

*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class HuffmanCodeDriver {

	public static void main(String[] args) throws FileNotFoundException 
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the text source file name: ");
		String source = input.nextLine();
		System.out.println("Enter the new compressed file name: ");
		String compressedSource = input.nextLine();
		System.out.println("Enter the decompressed file name: ");
		String decompressedSource = input.nextLine();
		Scanner fileScan = new Scanner(new File(source));
		PrintWriter out = new PrintWriter(compressedSource);

		HashMap<Integer, String> codes = HuffmanCoding.getCodes(fileScan);
		fileScan.close();
		fileScan = new Scanner(new File(source));
		HuffmanCoding.compress(out, fileScan, codes);
		out.close();
		out = new PrintWriter(decompressedSource);
		fileScan = new Scanner(new File(compressedSource));
		HuffmanCoding.decompress(out, fileScan);
		out.close();
		fileScan.close();
		input.close();
	}

}
