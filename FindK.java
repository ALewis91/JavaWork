import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class FindK {

	public static void main(String[] args) throws FileNotFoundException 
	{
		int[] n = new int[] {10, 25, 50, 75, 100, 150, 200, 500, 1000, 2000, 4000, 10000};
		int[] k = new int[5];
		double timeStart;
		double timeStop;
		double averageTime = 0.0;
		int current = 0;
		int size;
		int numberOfRuns = 10000;
		int[] list;
		int[] testList;
		double[][] testResults;
		
		
		list = new int[20];
		System.out.println("Test Case N = 20");	
		createList(list, 20);
		getKValues(list, k);
		functionTest(list, k);

		PrintWriter pw = new PrintWriter(new File("Find_Kth_Data.csv"));
		pw.write("Time Needed(ns) to Find Kth Element\n");
		current = 0;
		for (int i = 0; i < n.length; i++)
		{
			size = n[current];
			list = new int[size];
			createList(list, Integer.MAX_VALUE);
			getKValues(list, k);
			testResults = new double[4][5];
			for (int w = 0; w < k.length; w++)
			{
				for (int x = 0; x < numberOfRuns; x++)
				{
					testList = Arrays.copyOf(list, list.length);
					timeStart = System.nanoTime();
					algorithm1(testList, k[w]);
					timeStop = System.nanoTime();
					averageTime += timeStop - timeStart;
				}
				averageTime /= numberOfRuns;
				testResults[0][w] = averageTime;
			}
			printTimeStamp("Alg1 ", size);
			averageTime = 0;
			
			for (int w = 0; w < k.length; w++)
			{
				for (int x = 0; x < numberOfRuns; x++)
				{
					testList = Arrays.copyOf(list, list.length);
					timeStart = System.nanoTime();
					algorithm2(testList, 0, size-1, k[w]);
					timeStop = System.nanoTime();
					averageTime += timeStop - timeStart;
				}
				averageTime /= (numberOfRuns*1.0);
				testResults[1][w] = averageTime;
			}
			printTimeStamp("Alg2 ", size);
			averageTime = 0;
			
			for (int w = 0; w < k.length; w++)
			{
				for (int x = 0; x < numberOfRuns; x++)
				{
					testList = Arrays.copyOf(list, list.length);
					timeStart = System.nanoTime();
					algorithm3(testList, 0, size-1, k[w]);
					timeStop = System.nanoTime();
					averageTime += timeStop - timeStart;
				}
				averageTime /= (numberOfRuns*1.0);
				testResults[2][w] = averageTime;
			}
			printTimeStamp("Alg3 ", size);
			averageTime = 0;
			
			for (int w = 0; w < k.length; w++)
			{
				for (int x = 0; x < numberOfRuns; x++)
				{
					testList = Arrays.copyOf(list, list.length);
					timeStart = System.nanoTime();
					algorithm4(testList, 0, size-1, k[w]);
					timeStop = System.nanoTime();
					averageTime += timeStop - timeStart;
				}
				averageTime /= (numberOfRuns*1.0);
				testResults[3][w] = averageTime;
			}
			printTimeStamp("Alg4 ", size);
			averageTime = 0;
			
			pw.write("\nN = " + size);
			pw.write("\nK-Value,Algorithm 1,Algorithm 2,Algorithm 3,Algorithm 4\n");
			for (int x = 0 ; x < k.length; x++)
			{
				pw.write(k[x]+1 + ",");
				for (int y = 0; y < 4; y++)
				{
					pw.format("%.1f", testResults[y][x]);
					pw.write(',');
				}
				pw.write('\n');
			}
			pw.flush();
			current++;
		}
		pw.close();
	}
	

	
	public static void printTimeStamp(String m, int size)
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		System.out.print(m);
		System.out.printf("%-7d", size);	
		System.out.println("\t" + strDate + "\n");
	}
	
	public static void functionTest(int[] a, int[] k)
	{
		System.out.println("Original Array");
		printArray(a);
		System.out.println();
		for (int x = 0; x < k.length; x++)
		{
			System.out.println("K: " + (k[x]+1));
			System.out.println("Algorithm 1: " + algorithm1(Arrays.copyOf(a, a.length), k[x]));
			System.out.println("Algorithm 2: " + algorithm2(Arrays.copyOf(a, a.length), 0, a.length-1, k[x]));
			System.out.println("Algorithm 3: " + algorithm3(Arrays.copyOf(a, a.length), 0, a.length-1, k[x]));
			System.out.println("Algorithm 4: " + algorithm4(Arrays.copyOf(a, a.length), 0, a.length-1, k[x]));
		}
	}
	
	public static void printArray(int[] a)
	{
		for (int x = 0; x < a.length; x++)
			System.out.print(a[x] + " ");
	}
	public static void createList(int[] a, int s)
	{
		Random rand = new Random();
		int n = a.length;
		for (int x = 0; x < n; x++)
			a[x] = rand.nextInt(s)+ 1;
	}	
	public static void getKValues(int[] a, int[] k)
	{
		k[0] = 0;
		k[1] = (a.length-1)/4;
		k[2] = (a.length-1)/2;
		k[3] = (int) ((a.length-1)*(3.0/4));
		k[4] = a.length - 1;
	}
	public static void swap(int[] a, int i, int j)
	{
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	public static int algorithm1(int[] a, int k)
	{
		mergeSort(a);
		return a[k];
	}
	public static int algorithm2(int[] a, int low, int high, int k)
	{	
		int pivot = partition(a, low, high);
		while (pivot != k)
		{
			if (k < pivot)
				high = pivot - 1;
			else if (k > pivot)
				low = pivot + 1;
			pivot = partition (a, low, high);
		}
		return a[pivot];
	}	
	public static int algorithm3(int[] a, int low, int high, int k)
	{
		int pivot = partition(a, low, high);
		if (k == pivot)
			return a[k];
		else if (k < pivot)
			return algorithm3(a, low, pivot - 1, k);
		else
			return algorithm3(a, pivot + 1, high, k);
		
	}	
	public static int algorithm4(int[] a, int low, int high, int k)
	{
		int r = 5;
		int n = high - low + 1;
		if (n <= r)
		{
			insertionSort(a);
			return a[k];
		}
		
		int[] subA;
		int[] M = new int[n/r];
		for (int x = 0; x < n/r; x++)
		{
			subA = Arrays.copyOfRange(a, x*r+low, x*r+low+r);
			insertionSort(subA);
			M[x] = subA[r/2];
		}
		

		int V = algorithm4(M, 0, n/r-1, n/r/2);
		for (int x = 0; x < a.length; x++)
		{
			if (a[x] == V) 
			{
				swap(a, x, low);
				break;
			}
		}
		
		int pivot = partition(a, low, high);
		if (k == pivot)
			return a[k];
		else if (k < pivot)
			return algorithm4(a, low, pivot - 1, k);
		else
			return algorithm4(a, pivot + 1, high, k);
	}	
	public static int partition(int[] a, int low, int high)
	{
		int pivot = low;
		//Increment l to start at pivot + 1 element
		low++;
		//Partition
		while (low <= high)
		{
			while (low <= high && a[low] <= a[pivot])
				low++;
			while (high >= low && a[high] > a[pivot])
				high--;
			if (low < high)
				swap(a, low, high);	
		}
		//Put pivot value in place
		swap(a, pivot, high);
		//Return index of pivot
		return high;
	}
	public static void insertionSort(int[] s)
	{
		int n = s.length;
		for (int x = 1; x < n; x++)
		{
			int y = x;
			while (y > 0 && s[y-1] > s[y])
			{
				swap(s, y, y-1);
				y--;
			}
		}
	}
	public static void merge(int[] s1, int[] s2, int[] s)
	{
		int i = 0, j = 0;	
		while (i < s1.length && j < s2.length)
		{
			if (s1[i] <= s2[j])
				s[i+j] = s1[i++];
			else
				s[i+j] = s2[j++];
		}	
		while (i < s1.length)
			s[i+j] = s1[i++];
		while (j < s2.length)
			s[i+j] = s2[j++];
	}
	public static void mergeSort(int[] s)
	{
		int n = s.length;
		if (n < 2)
			return;
		int mid = n/2;
		int[] s1 = Arrays.copyOfRange(s, 0, mid);
		int[] s2 = Arrays.copyOfRange(s, mid, n);
		mergeSort(s1);
		mergeSort(s2);		
		merge(s1, s2, s);
	}	
}


