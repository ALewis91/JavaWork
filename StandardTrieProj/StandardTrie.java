import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class StandardTrie 
{
	private trieNode root;
	
	private class trieNode
	{
		public char c;
		private LinkedList<trieNode> children;
		
		trieNode(char a)
		{
			c = a;
			children = new LinkedList<trieNode>();
		}
		
		private void addLetter(char a)
		{

			if (a == '$')
			{
				trieNode temp = new trieNode('$');
				children.addFirst(temp);
			}
			else
			{
				trieNode temp = new trieNode(a);
				children.add(temp);
			}
		}
		
		private char getChar()
		{
			return c;
		}
		
		private trieNode locate(char a)
		{
			if (children.size() > 0)
			{
				for (int x = 0; x < children.size(); x++)
				{
					if (children.get(x).getChar() == a)
						return children.get(x);
				}
				return null;
			}
			else
				return null;
		}
		
	}
	
	StandardTrie(String text) throws FileNotFoundException
	{
		root = new trieNode('@');
		Scanner fileScan = new Scanner(new File(text));
		fileScan.useDelimiter("(\\p{javaWhitespace}|\\.|,)+");
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		String nextWord;
		trieNode pos;
		while (fileScan.hasNext())
		{
			nextWord = fileScan.next().toLowerCase();
			if (words.get(nextWord) == null)
			{
				pos = root;
				words.put(nextWord, 1);
				for (int x = 0; x < nextWord.length(); x++)
				{
					if (pos.locate(nextWord.charAt(x)) == null)
						pos.addLetter(nextWord.charAt(x));
					pos = pos.locate(nextWord.charAt(x));
				}
				pos.addLetter('$');
			}
		}
		fileScan.close();
	}
	
	public boolean contains(String pattern)
	{
		trieNode pos = root;
		
		for (int x = 0; x < pattern.length(); x++)
		{
			if (pos.locate(pattern.charAt(x)) == null)
				return false;
			pos = pos.locate(pattern.charAt(x));
		}
		if (pos.locate('$') != null)
			return true;
		else
			return false;
	}

}
