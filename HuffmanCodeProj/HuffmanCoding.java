import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.PriorityQueue;

public class HuffmanCoding 
{
	private static HuffmanNode tree;
	private static final int N_ASCII = 128;
	private static int[] frequency;
	
	private static HuffmanNode getTree(Scanner input)
	{
		frequency = new int[N_ASCII];
		String text = "";
		HuffmanNodeComparator h = new HuffmanNodeComparator();
		PriorityQueue<HuffmanNode> pq = new PriorityQueue<HuffmanNode>(1, h);
		
		while (input.hasNextLine())
		{
			text += input.nextLine();
			if (input.hasNextLine())
				text += '\n';
		}

		for (int x = 0; x < text.length(); x++)
			frequency[(int)text.charAt(x)]++;

		
		
		for (int x = 0; x < N_ASCII; x++)
		{
			if (frequency[x] > 0)
			{
				HuffmanNode temp = new HuffmanNode();
				temp.setChar(((char)x));
				temp.setFreq(frequency[x]);
				pq.add(temp);
			}
		}
		
		while (pq.size() > 1)
		{
			HuffmanNode left = pq.poll();
			HuffmanNode right = pq.poll();
			HuffmanNode par = new HuffmanNode();
			par.setFreq(left.getFreq() + right.getFreq());
			par.setLeft(left);
			par.setRight(right);
			left.setPar(par);
			right.setPar(par);
			pq.add(par);
		}
		return pq.poll();
		
	}
	
	public static HashMap<Integer, String> getCodes(Scanner input)
	{
		String code = "";
		HuffmanNode root = getTree(input);
		tree = root;
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		inorder(root, code, hm);
		
		return hm;
	}
	
	public static void compress(PrintWriter out, Scanner input, HashMap<Integer, String> codes) throws FileNotFoundException
	{
		String line = "";
		int numChars = 0;
		int numBits = 0;
		for (int x = 0; x < N_ASCII; x++)
		{
			if (codes.get(x) != null)
			{
				if (x == 10)
				{
					out.println();
					out.println(" " + codes.get(x));
				}
				else
					out.println((char)x + " " + codes.get(x));
			}
		}
		out.println("*****");
		for (int x = 0; x < N_ASCII; x++)
		{
			if (frequency[x] > 0)
			{
				numChars += frequency[x];
				numBits += codes.get(x).length() * frequency[x];
			}
		}
		out.println("Number of characters: " + numChars);
		out.println("Number of bits: " + numBits);
		out.println("$");
		while (input.hasNext())
		{
			line = input.nextLine();
			for (int x = 0; x < line.length(); x++)
			{
				out.print(codes.get((int)line.charAt(x)));
			}
			if (input.hasNext())
			{
				out.print(codes.get((int)'\n'));
			}
		}
	}
	
	public static void decompress(PrintWriter out, Scanner in) throws FileNotFoundException
	{
		HuffmanNode current = tree;
		String line = in.nextLine();
		while (in.hasNext() && !line.equals("$"))
			line = in.nextLine();
		line = in.nextLine();
		for (int x = 0; x < line.length(); x++)
		{
			if (current.isExternal())
			{
				if (current.getChar() == 10)
					out.println();
				else
					out.print(current.getChar());
				current = tree;
				x--;
			}
			else if (line.charAt(x) == '0')
				current = current.getLeft();
			else
				current = current.getRight();
		}
		if (current.getChar() == 10)
			out.println();
		else
			out.print(current.getChar());

	}
	
	private static void inorder(HuffmanNode root, String code, HashMap<Integer, String> hm)
	{	
		if (!root.isExternal())
		{
			code += "0";
			inorder(root.getLeft(), code, hm);
		}
		
		hm.put((int)root.getChar(), code);		
		code = code.substring(0, code.length() - 1);
		
		if (!root.isExternal())
		{
			code += "1";
			inorder(root.getRight(), code, hm);
		}
		
	}
}
