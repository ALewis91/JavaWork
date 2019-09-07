/*  Program:     Project 3
    Author:      Aaron Lewis
    Class:	     CSCI230 F
    Date:	     05/11/2018
    Description: Gather data on the BM and KMP
    	pattern matching algorithms by searching
    	a text chosen from a menu and searching 
    	for user-input patterns. Search time, 
    	results, comparisons, and average comparison
    	per pattern character is displayed after 
    	each search.

    I certify that the code below is my own work.
	
	Exception(s): N/A

*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class PatternMatchingDriver {

	public static void main(String[] args) throws FileNotFoundException 
	{
		Scanner input = new Scanner(System.in);
		String fileName = "";
		Vector<Integer> bmComparisons = new Vector<Integer>();
		Vector<Integer>  kmpComparisons = new Vector<Integer>();
		Vector<Integer> bmIndex = new Vector<Integer>();
		Vector<Integer> kmpIndex = new Vector<Integer>();
		Vector<Double> bmAvg = new Vector<Double>();
		Vector<Double> kmpAvg = new Vector<Double>();
		Vector<Double> bmTime = new Vector<Double>();
		Vector<Double> kmpTime = new Vector<Double>();
		double nsPerMs = 1000000.0;
		
		long start, end;
		int index = 0;
		
		while (!fileName.equals("quit"))
		{
			fileName = mainMenu(input);
			
			if (!fileName.equals("quit"))
			{
				File textFile = new File(fileName);
				Scanner docScan = new Scanner(textFile);
				String text = "";			
				while(docScan.hasNext())
				{
					String nextLine = docScan.nextLine().toLowerCase();
					text += nextLine + "\n";
				}			
				docScan.close();
				
				String pattern = "";
				PatternMatch.clear();
				do
				{
					System.out.print("\nEnter a pattern to search for,");
					System.out.println(" -1 to return to main menu");
					pattern = input.nextLine().toLowerCase();
					if (!pattern.equals("-1"))
					{
						

						start = System.nanoTime();
						kmpIndex.add(PatternMatch.KMPMatch(text, pattern));
						end = System.nanoTime();
						kmpComparisons.add(PatternMatch.getComparisons());
						kmpAvg.add(kmpComparisons.elementAt(index)/(double)pattern.length());
						kmpTime.add(end/nsPerMs - start/nsPerMs);
						PatternMatch.clear();
						
						start = System.nanoTime();
						bmIndex.add(PatternMatch.BMMatch(text, pattern));
						end = System.nanoTime();
						bmComparisons.add(PatternMatch.getComparisons());
						bmAvg.add(bmComparisons.elementAt(index)/(double)pattern.length());
						bmTime.add(end/nsPerMs - start/nsPerMs);
						PatternMatch.clear();
						
						
						printResults(index, bmComparisons, kmpComparisons, bmAvg, kmpAvg, bmTime, kmpTime, bmIndex, kmpIndex);
						index++;
					}
	
				} while (!pattern.equals("-1"));
			}
			
		}
	}
	
	public static String mainMenu(Scanner input)
	{
		System.out.println("Pattern Matching Program");
		System.out.println("\nSelect a file:");
		System.out.println("1. usdeclarPC.txt");
		System.out.println("2. humanDNA.txt");
		System.out.println("3. Quit");
		String selection = input.nextLine();
		while (!selection.equals("1") &&
			   !selection.equals("2") &&
			   !selection.equals("3"))
		{
			System.out.println("Error: select an option from the menu!");
			selection = mainMenu(input);
		}
		
		if (selection.equals("1"))
			selection = "usdeclarPC.txt";
		else if (selection.equals("2"))
			selection = "humanDNA.txt";
		else
			selection = "quit";
				
		return selection;
	}
	
	public static void printResults(int index, Vector<Integer> bmComp, 
			Vector<Integer> kmpComp, Vector<Double> bmAvg, Vector<Double> kmpAvg,
			Vector<Double> bmTime, Vector<Double> kmpTime, Vector<Integer> bmIndex,
			Vector<Integer> kmpIndex)
	{
		System.out.println("Results\t\t\tBM\t\tKMP");
		System.out.println("Match Index\t\t" + bmIndex.elementAt(index) + "\t\t" + kmpIndex.elementAt(index));
		System.out.print("Time Elapsed(ms)\t");
		System.out.printf("%.4f",bmTime.elementAt(index));
		System.out.print("\t\t");
		System.out.printf("%.4f",kmpTime.elementAt(index));
		System.out.println();
		System.out.println("Total Comparisons\t" + bmComp.elementAt(index) + "\t\t" + kmpComp.elementAt(index));
		System.out.print("   Avg per Char\t\t");
		System.out.printf("%.2f", bmAvg.elementAt(index));
		System.out.print("\t\t");
		System.out.printf("%.1f", kmpAvg.elementAt(index));
		System.out.println();

	}
}
