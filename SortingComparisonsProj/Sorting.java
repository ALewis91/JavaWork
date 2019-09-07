import java.util.Comparator;
import java.util.Vector;

public class Sorting 
{
	private static int dataMoves = 0;
	private static int keyCompares = 0;
	public static <K> void selectionSort(Vector<K> S, Comparator<K> comp)
	{
		K temp;
		int min;
		for (int x = 0; x < S.size() - 1; x++)
		{
			min = x;
			for (int y = x + 1 ; y < S.size(); y++)
			{
				if (comp.compare(S.elementAt(y), S.elementAt(min)) < 0)
					min = y;
				keyCompares++;
			}
			temp = S.elementAt(x);
			S.setElementAt(S.elementAt(min), x); 
			S.setElementAt(temp,  min);
			dataMoves += 3;
		}
	}
	
	public static <K> void insertionSort(Vector<K> S, Comparator<K> comp)
	{
		int current;
		for (int x = 1; x < S.size(); x++)
		{
			current = x;
			K temp = S.elementAt(current);
			dataMoves++;
			while (current > 0 && comp.compare(S.elementAt(current - 1), temp) > 0)
			{
				S.setElementAt(S.elementAt(current-1), current);
				keyCompares++;
				dataMoves++;
				current--;
			}

			S.setElementAt(temp, current);
			if (current > 0)
				keyCompares++;
			dataMoves++;
			
		}
	}
	
	public static <K> void quickSort(Vector<K> S, Comparator<K> comp)
	{
		int n = S.size();
		if (n < 2)
			return;
		
		K pivot;
		K first = S.elementAt(0);
		K middle = S.elementAt(n/2);
		K last = S.elementAt(n - 1);
		
		if (comp.compare(first, middle) < 0)
		{
			keyCompares++;
			if (comp.compare(first, last) >= 0)
			{
					pivot = first;
					keyCompares++;
			}
			else if (comp.compare(middle, last) < 0)
			{
				pivot = middle;
				keyCompares += 2;
			}
			else
			{
				pivot = last;
				keyCompares += 2;
			}
		}
		else
		{
			keyCompares++;
			if (comp.compare(first, last) < 0)
			{
				pivot = first;
				keyCompares++;
			}
			else if (comp.compare(middle, last) > 0)
			{
				pivot = middle;
				keyCompares += 2;
			}
			else
			{
				pivot = last;
				keyCompares += 2;
			}
		}

		Vector<K> L = new Vector<>();
		Vector<K> E = new Vector<>();
		Vector<K> G = new Vector<>();
		
		for(int x = 0; x < S.size(); x++)
		{
			int c = comp.compare(S.elementAt(x), pivot);
			keyCompares++;
			if ( c < 0 )
			{
				L.add(S.elementAt(x));
				dataMoves++;
			}
			else if ( c == 0 )
			{
				E.add(S.elementAt(x));
				dataMoves++;
			}
			else
			{
				G.add(S.elementAt(x));
				dataMoves++;
			}
		}
		S.clear();
		quickSort(L, comp);
		quickSort(G, comp);
		
		for (int x = 0; x < L.size(); x++)
		{
			S.add(L.elementAt(x));
			dataMoves++;
		}
		for (int x = 0; x < E.size(); x++)
		{
			S.add(E.elementAt(x));
			dataMoves++;
		}
		for (int x = 0; x < G.size(); x++)
		{
			S.add(G.elementAt(x));
			dataMoves++;
		}
		L.clear();
		E.clear();
		G.clear();
	}
	
	public static <K> void mergeSort(Vector<K> S, Comparator<K> comp)
	{
		int n = S.size();
		if (n < 2)
			return;		
		int mid = n/2;
		Vector<K> S1 = new Vector<>();
		Vector<K> S2 = new Vector<>();		
		for (int x = 0; x < mid; x++)
		{
			S1.add(S.elementAt(x));
			dataMoves++;
		}
		for (int x = mid; x < n; x++)
		{
			S2.add(S.elementAt(x));
			dataMoves++;
		}		
		mergeSort(S1, comp);
		mergeSort(S2, comp);
			
		int i = 0;
		int j = 0;
		while (i + j < S.size())
		{
			if (comp.compare(S1.elementAt(i), S2.elementAt(j)) < 0)
			{
				S.setElementAt(S1.elementAt(i), i + j);
				i++;
				dataMoves++;
				keyCompares++;
			}
			else
			{
				S.setElementAt(S2.elementAt(j), i + j);
				j++;
				dataMoves++;
				keyCompares++;
			}
			
			if (i == S1.size())
			{
				while (j < S2.size())
				{
					S.setElementAt(S2.elementAt(j), i + j);
					j++;
					dataMoves++;
				}
			}
			
			if (j == S2.size())
			{
				while (i < S1.size())
				{
					S.setElementAt(S1.elementAt(i), i + j);
					i++;
					dataMoves++;
				}
			}
		}
	}
	
	public static int getDataMoves()
	{
		return dataMoves;
	}
	public static int getKeyCompares()
	{
		return keyCompares;
	}
	public static void clear()
	{
		dataMoves = 0;
		keyCompares = 0;
	}
}
