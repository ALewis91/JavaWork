public class PatternMatch 
{
	private static int comparisons = 0;
	
	public static int BMMatch(String text, String pattern)
	{
		int[] last = buildLast(pattern);
		
		int n = text.length();
		int m = pattern.length();
		
		int i = m - 1;
		int j = m - 1;
		
		if (i > n - 1)
			return - 1;
		do 
		{
			if (pattern.charAt(j) == text.charAt(i))
			{
				if (j == 0)
				{
					comparisons++;
					return i;
				}
				else
				{
					i--;
					j--;
				}
			}
			else
			{
				i = i + m - Math.min(j,  1+ last[(int)text.charAt(i)]);
				j = m - 1;
			}
			comparisons++;
		} while ( i <= n - 1);
		return -1;
	}
	
	private static int[] buildLast(String pattern)
	{
		final int N_ASCII = 128;
		int[] last = new int[N_ASCII];

		for(int i = 0; i < pattern.length(); i++)
			last[(int)pattern.charAt(i)] = i;
		
		return last;
	}
	
	
	private static int[] computeFail(String pattern)
	{
		int[] fail = new int[pattern.length()];
		fail[0] = 0;
		int m = pattern.length();
		int i = 1;
		int j = 0;
		
		while (i < m)
		{
			if(pattern.charAt(j) == pattern.charAt(i))
			{
				fail[i] = j+1;
				i++;
				j++;
			}
			else if (j > 0)
				j = fail[j - 1];
			else
			{
				fail[i] = 0;
				i++;
			}			
		}

		return fail;
	}
	
	public static int KMPMatch(String text, String pattern)
	{
		int n = text.length();
		int m = pattern.length();
		int[] fail = computeFail(pattern);
		int i = 0;
		int j = 0;
		while (i < n)
		{
			if (pattern.charAt(j) == text.charAt(i))
			{
				if (j == m - 1)
				{
					comparisons++;
					return i - m + 1;
				}
				i++;
				j++;
			}
			else if (j > 0)
				j = fail[j - 1];
			else
				i++;
			
			comparisons++;
		}
		
		return -1;
		
	}
	
	public static int getComparisons()
	{
		return comparisons;
	}
	
	public static void clear()
	{
		comparisons = 0;
	}
}
