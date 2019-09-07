import java.io.FileNotFoundException;
import java.util.Scanner;

public class TrieDriver {

	public static void main(String[] args) throws FileNotFoundException 
	{
		System.out.print("Enter filename: ");
		Scanner input = new Scanner(System.in);
		String fileName = input.nextLine();
		String string = "";
		StandardTrie trie = new StandardTrie(fileName);
		System.out.print("\nEnter a string to search for, quit to end: ");
		string = input.nextLine();
		while (!string.equals("quit"))
		{
			if (trie.contains(string))
				System.out.println(fileName + " contains " + string + "!");
			else
				System.out.println(fileName + " does not contain " + string + "!");
			
			System.out.print("\nEnter a string to search for, quit to end: ");
			string = input.nextLine();
		}
		
		input.close();
	}

}
