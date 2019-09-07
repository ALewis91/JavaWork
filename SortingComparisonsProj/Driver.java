/*  Program:     Project 2
    Author:      Aaron Lewis
    Class:	     CSCI230 F
    Date:	     04/20/2018
    Description: Gather data on sorting algorithms
    	including selection sort, insertion sort,
    	quick sort, and merge sort. Collect number
    	of key comparisons and data moves for each
    	sorting of a list created from a data file.
    	Compare both string and integer key values
    	for each of the different algorithms and
    	print to the file p2Results.txt.

    I certify that the code below is my own work.
	
	Exception(s): N/A

*/

import java.util.Vector;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Driver 
{
	public static void main(String[] args) throws FileNotFoundException 
	{
		String sortMethod;
		String inputFile;
		int numValues;
		String keyDataType;
		int numKeyComps;
		int numDataMoves;
		long timeElapsedMs;
		boolean done = false;
		Vector<Entry<Integer, String>> intList = 
				new Vector<Entry<Integer, String>>();
		Vector<Entry<String, Integer>> strList = 
				new Vector<Entry<String, Integer>>();
		PrintWriter reportWriter = new PrintWriter("p2Results.txt");
		
		System.out.println("Author: Aaron Lewis\n");
		System.out.println("Comparing Sorting Algorithms\n");
		reportWriter.println("Sorting Algorithm Program Report");
		
		while (!done)
		{
			inputFile = fileSelection();
			sortMethod = sortSelection();
			keyDataType = keyDataTypeSelection();

			if (keyDataType.equals("Integer"))
			{
				buildIntKeyVector(intList, inputFile);
				numValues = intList.size();
				timeElapsedMs = sortInt(intList, sortMethod);
				numKeyComps = Sorting.getKeyCompares();
				numDataMoves = Sorting.getDataMoves();
				writeResultsIntKey(sortMethod, inputFile, numValues, 
						numKeyComps, numDataMoves, timeElapsedMs, 
						intList, reportWriter);
				intList.clear();
			}
			else
			{
				buildStrKeyVector(strList, inputFile);
				numValues = strList.size();
				timeElapsedMs = sortStr(strList, sortMethod);
				numKeyComps = Sorting.getKeyCompares();
				numDataMoves = Sorting.getDataMoves();
				writeResultsStrKey(sortMethod, inputFile, numValues, 
						numKeyComps, numDataMoves, timeElapsedMs, 
						strList, reportWriter);
				strList.clear();
			}
			Sorting.clear();
			System.out.println("Results written to p2Report.txt file...\n");
			done = mainMenu();
		}
		reportWriter.close();
	}

	public static boolean mainMenu()
	{
		String selection = "";
		Scanner scan = new Scanner(System.in);
		while (!selection.equals("1") && !selection.equals("2"))
		{
			System.out.print("Would you like to test another sorting");
			System.out.println(" algorithm?");
			System.out.println("  1 - Yes");
			System.out.println("  2 - No");
			
			selection = scan.nextLine();
			if (!selection.equals("1") && !selection.equals("2"))
				printError();
						
		}
		
		if (selection.equals("1"))
			return false;
		else
			return true;
	}
	
	public static String fileSelection()
	{
		String selection = "";
		Scanner scan = new Scanner(System.in);
		while (!selection.equals("1") && !selection.equals("2"))
		{
			System.out.println("Select a file to build the list from:");
			System.out.println("  1 - \"small1k.txt\"");
			System.out.println("  2 - \"large100k.txt\"");
			
			selection = scan.nextLine();
			if (!selection.equals("1") && !selection.equals("2"))
				printError();
		}

		if (selection.equals("1"))
			selection = "small1k.txt";
		if (selection.equals("2"))
			selection = "large100k.txt";
		
		return selection;
	}
	
	public static String sortSelection()
	{
		String selection = "";
		Scanner scan = new Scanner(System.in);
		while (!selection.equals("1") && !selection.equals("2")
				&& !selection.equals("3") && !selection.equals("4"))
		{
			System.out.println("Select a sorting method:");
			System.out.println("  1 - Selection Sort");
			System.out.println("  2 - Insertion Sort");
			System.out.println("  3 - Quick Sort");
			System.out.println("  4 - Merge Sort");
			
			selection = scan.nextLine();
			
			if (!selection.equals("1") && !selection.equals("2")
					&& !selection.equals("3") && !selection.equals("4"))
				printError();
		}
		
		if (selection.equals("1"))
			selection = "Selection Sort";
		else if (selection.equals("2"))
			selection = "Insertion Sort";
		else if (selection.equals("3"))
			selection = "Quick Sort";
		else
			selection = "Merge Sort";
		
		return selection;
	}
	
	public static String keyDataTypeSelection()
	{
		String selection = "";
		Scanner scan = new Scanner(System.in);
		while (!selection.equals("1") && !selection.equals("2"))
		{
			System.out.println("Select which type of entry to use:");
			System.out.println("  1 - Key:Integer, Value:String");
			System.out.println("  2 - Key:String, Value:Integer");
			
			selection = scan.nextLine();
			if (!selection.equals("1") && !selection.equals("2"))
				printError();
		}
		
		if (selection.equals("1"))
			selection = "Integer";
		else
			selection = "String";
		
		return selection;
	}
	
	public static void buildIntKeyVector(Vector<Entry<Integer, String>> v, String fileName) throws FileNotFoundException
	{
		Scanner fileScan = new Scanner(new File(fileName));
		Integer key;
		String value;
		Entry<Integer, String> e;
		while (fileScan.hasNext())
		{
			key = fileScan.nextInt();
			value = key.toString();
			e = new Entry<Integer, String>(key, value);
			v.add(e);
		}
		fileScan.close();
	}
	
	public static void buildStrKeyVector(Vector<Entry<String, Integer>> v, String fileName) throws FileNotFoundException
	{
		Scanner fileScan = new Scanner(new File(fileName));
		String key;
		Integer value;
		Entry<String, Integer> e;
		while (fileScan.hasNext())
		{
			key = fileScan.next();
			value = Integer.parseInt(key);
			e = new Entry<String, Integer>(key, value);
			v.add(e);
		}
		fileScan.close();
	}
	
	public static long sortInt(Vector<Entry<Integer, String>> v, String sortMethod)
	{
		CompInteger intComp = new CompInteger();
		
		long start = System.currentTimeMillis();
		
		if (sortMethod.equals("Selection Sort"))
			Sorting.selectionSort(v, intComp);
		else if (sortMethod.equals("Insertion Sort"))
			Sorting.insertionSort(v, intComp);
		else if (sortMethod.equals("Quick Sort"))
			Sorting.quickSort(v, intComp);
		else
			Sorting.mergeSort(v, intComp);
		long end = System.currentTimeMillis();
		
		return end - start;
	}
	
	public static long sortStr(Vector<Entry<String, Integer>> v, String sortMethod)
	{
		CompString strComp = new CompString();
		
		long start = System.currentTimeMillis();
		
		if (sortMethod.equals("Selection Sort"))
			Sorting.selectionSort(v, strComp);
		else if (sortMethod.equals("Insertion Sort"))
			Sorting.insertionSort(v, strComp);
		else if (sortMethod.equals("Quick Sort"))
			Sorting.quickSort(v, strComp);
		else
			Sorting.mergeSort(v, strComp);
		long end = System.currentTimeMillis();

		return end - start;
	}
	
	public static void writeResultsIntKey(String method, String file, int numVals,
										int numKeyComps, int numDataMoves, long timeMs, 
										Vector<Entry<Integer,String>> v, PrintWriter outFile)
	{
		outFile.println();
		outFile.println("Sorting Method:         " + method);
		outFile.println("Input File Name:        " + file);
		outFile.println("Number of Values:       " + numVals);
		outFile.println("Key Data Type:          Integer");
		outFile.println("Number of Key Compares: " + numKeyComps);
		outFile.println("Number of Data Moves:   " + numDataMoves);
		outFile.println("Time to Sort(ms):       " + timeMs);
		outFile.println("First 5 Pairs");
		for (int x = 0; x < 5; x++)
		{
			outFile.print("  " + v.elementAt(x).getKey());
			outFile.println(", \"" + v.elementAt(x).getValue() + "\" ");
		}
		outFile.println("Last 5 Pairs");
		for (int x = v.size() - 6; x < v.size(); x++)
		{
			outFile.print("  " + v.elementAt(x).getKey());
			outFile.println(", \"" + v.elementAt(x).getValue() + "\"");
		}
	}

	public static void writeResultsStrKey(String method, String file, int numVals,
			int numKeyComps, int numDataMoves, long timeMs, 
			Vector<Entry<String, Integer>> v, PrintWriter outFile)
	{
		outFile.println();
		outFile.println("Sorting Method:   " + method);
		outFile.println("Input File Name:  " + file);
		outFile.println("Number of Values: " + numVals);
		outFile.println("Key Data Type:    String");		outFile.println("Number of Key Compares: " + numKeyComps);
		outFile.println("Number of Data Moves:   " + numDataMoves);
		outFile.println("Time to Sort(ms): " + timeMs);
		outFile.println("First 5 Pairs");
		for (int x = 0; x < 5; x++)
		{
			outFile.print("  \"" + v.elementAt(x).getKey());
			outFile.println("\", " + v.elementAt(x).getValue() + " ");
		}
		outFile.println("Last 5 Pairs");
		for (int x = v.size() - 6; x < v.size(); x++)
		{
			outFile.print("  \"" + v.elementAt(x).getKey());
			outFile.println("\", " + v.elementAt(x).getValue() + " ");
		}
	}
	
	public static void printIntList(Vector<Entry<Integer, String>> v)
	{
		for (int x = 0; x < v.size(); x++)
		{
			System.out.print(v.elementAt(x).getKey() + " ");
			if (x % 10 == 9)
				System.out.println("");
		}
	}
	
	public static void printError()
	{
		System.out.println("Please select an option from the menu!");
	}

}
