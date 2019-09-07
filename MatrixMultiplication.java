import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MatrixMultiplication {

	public static void main(String[] args) throws FileNotFoundException 
	{
		int size = 2;	
		final int MILLION = 1000000;
		double timeStart;
		double timeStop;
		double averageTime = 0;
		int numberOfRuns = 10;
		int[][] a;
		int[][] b;
		int[][] c;
		Random rand = new Random();
		
		System.out.println("Test Cases");
		while (size < Math.pow(2, 3))
		{
			a = new int[size][size];
			b = new int[size][size];
			for (int x = 0; x < size; x++)
			{
				for (int y = 0; y < size; y++)
				{
					a[x][y] = rand.nextInt(5) + 1;
					b[x][y] = rand.nextInt(5) + 1;
				}
			}
			
			System.out.println("\nMatrix A");
			for (int x = 0; x < size; x++)
			{
				for (int y = 0; y < size; y++)
					System.out.printf("%3s", a[x][y] + " ");
				System.out.println();
			}
		
			System.out.println("\nMatrix B");
			
			for (int x = 0; x < size; x++)
			{
				for (int y = 0; y < size; y++)
					System.out.printf("%3s", b[x][y] + " ");
				System.out.println();
			}
		
			System.out.println("\nMatrix C (Classical)");
			c = classicMultiplyMatrix(size, a, b);
			for (int x = 0; x < size; x++)
			{
				for (int y = 0; y < size; y++)
					System.out.printf("%4s", c[x][y] + " ");
				System.out.println();
			}
			
			System.out.println("\nMatrix C (Divide-and-Conquer)");
			c = DCMultiplyMatrix(size, a, b);
			for (int x = 0; x < size; x++)
			{
				for (int y = 0; y < size; y++)
					System.out.printf("%4s", c[x][y] + " ");
				System.out.println();
			}
			
			System.out.println("\nMatrix C (Strassen)");
			c = strassenMultiplyMatrix(size, a, b);
			for (int x = 0; x < size; x++)
			{
				for (int y = 0; y < size; y++)
					System.out.printf("%4s", c[x][y] + " ");
				System.out.println();
			}
			size *= 2;
		}
		
		System.out.println();
		
		size = 2;
		
		PrintWriter pw = new PrintWriter(new File("Matrix_Multiplication_Data2.csv"));
		pw.write("Time Needed(ms) to Multiply Two NxN Matrix\n\n");
		pw.write("Matrix Size,Classical,Divide-and-Conquer,Strassen\n");
		
		while (size < Math.pow(2, 15))
		{
			a = new int[size][size];
			b = new int[size][size];
			
			for (int x = 0; x < numberOfRuns; x++)
			{
				timeStart = System.nanoTime();
				c = classicMultiplyMatrix(size, a, b);
				timeStop = System.nanoTime();
				averageTime += timeStop - timeStart;
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = new Date();
				String strDate = sdfDate.format(now);
				System.out.print("Classical ");
				System.out.printf("%-7d", size);	
				System.out.println(" #" + (x+1) + "\t" + strDate + "\n");
			}
			averageTime /= numberOfRuns;
			pw.write("" + size + ",");
			pw.format("%.4f", averageTime/MILLION);
			pw.flush();
			averageTime = 0;
			
			for (int x = 0; x < numberOfRuns; x++)
			{
				timeStart = System.nanoTime();
				c = DCMultiplyMatrix(size, a, b);
				timeStop = System.nanoTime();
				averageTime += timeStop - timeStart;
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = new Date();
				String strDate = sdfDate.format(now);
				System.out.print("Div&Conq ");
				System.out.printf("%-7d", size);	
				System.out.println(" #" + (x+1) + "\t" + strDate + "\n");
			}
			averageTime /= numberOfRuns;
			pw.write(',');
			pw.format("%.4f", averageTime/MILLION);
			pw.flush();
			averageTime = 0;
			
			for (int x = 0; x < numberOfRuns; x++)
			{
				timeStart = System.nanoTime();
				c = strassenMultiplyMatrix(size, a, b);
				timeStop = System.nanoTime();
				averageTime += timeStop - timeStart;
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = new Date();
				String strDate = sdfDate.format(now);
				System.out.print("Strassen ");
				System.out.printf("%-7d", size);	
				System.out.println(" #" + (x+1) + "\t" + strDate + "\n");
			}
			averageTime /= numberOfRuns;
			pw.write(",");
			pw.format("%.4f", averageTime/MILLION);
			pw.write("\n");
			pw.flush();
			size *= 2;
		}
		pw.close();
	}

	public static int[][] classicMultiplyMatrix(int n, int[][] A, int[][] B)
	{
		int[][] C = new int[n][n];
		
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				int sum = 0;
				for (int k = 0; k < n; k++)
					sum += A[i][k] * B[k][j];
				C[i][j] = sum;
			}
		}
		return C;
	}
	
	public static int[][] DCMultiplyMatrix(int n, int[][] A, int[][] B)
	{
		int[][] C = new int[n][n];
		if (n <= 2)
		{
			C = classicMultiplyMatrix(n, A, B);
			return C;
		}
		else
		{
			int m = n/2;
			int[][] A11 = new int[m][m];
			int[][] A12 = new int[m][m];
			int[][] A21 = new int[m][m];
			int[][] A22 = new int[m][m];
			int[][] B11 = new int[m][m];
			int[][] B12 = new int[m][m];
			int[][] B21 = new int[m][m];
			int[][] B22 = new int[m][m];
			
			for (int x = 0; x < m; x++)
			{
				for (int y = 0; y < m; y++)
				{
					A11[x][y] = A[x][y];
					B11[x][y] = B[x][y];
					A12[x][y] = A[x][y+m];
					B12[x][y] = B[x][y+m];
					A21[x][y] = A[x+m][y];
					B21[x][y] = B[x+m][y];
					A22[x][y] = A[x+m][y+m];
					B22[x][y] = B[x+m][y+m];
				}
			}
			
			int[][] X1 = DCMultiplyMatrix(m, A11, B11);
			int[][] X2 = DCMultiplyMatrix(m, A12, B21);
			int[][] X3 = DCMultiplyMatrix(m, A11, B12);
			int[][] X4 = DCMultiplyMatrix(m, A12, B22);
			int[][] X5 = DCMultiplyMatrix(m, A21, B11);
			int[][] X6 = DCMultiplyMatrix(m, A22, B21);
			int[][] X7 = DCMultiplyMatrix(m, A21, B12);
			int[][] X8 = DCMultiplyMatrix(m, A22, B22);

			for (int x = 0; x < m; x++)
			{
				for (int y = 0; y < m; y++)
				{
					C[x][y] = X1[x][y] + X2[x][y];
					C[x][y+m] = X3[x][y] + X4[x][y];
					C[x+m][y] = X5[x][y] + X6[x][y];
					C[x+m][y+m] = X7[x][y] + X8[x][y];
				}
			}
			return C;
		}
	}
	
	public static int[][] strassenMultiplyMatrix(int n, int[][] A, int[][] B)
	{
		int[][] C = new int[n][n];
		if (n <= 2)
		{
			C = classicMultiplyMatrix(n, A, B);
			return C;
		}
		else
		{
			int m = n/2;
			int[][] A11 = new int[m][m];
			int[][] A12 = new int[m][m];
			int[][] A21 = new int[m][m];
			int[][] A22 = new int[m][m];
			int[][] B11 = new int[m][m];
			int[][] B12 = new int[m][m];
			int[][] B21 = new int[m][m];
			int[][] B22 = new int[m][m];
			
			for (int x = 0; x < m; x++)
			{
				for (int y = 0; y < m; y++)
				{
					A11[x][y] = A[x][y];
					B11[x][y] = B[x][y];
					A12[x][y] = A[x][y+m];
					B12[x][y] = B[x][y+m];
					A21[x][y] = A[x+m][y];
					B21[x][y] = B[x+m][y];
					A22[x][y] = A[x+m][y+m];
					B22[x][y] = B[x+m][y+m];
				}
			}
	
			int[][]	P = strassenMultiplyMatrix(m, addMatrix(m, A11, A22), addMatrix(m, B11, B22));
			int[][] Q = strassenMultiplyMatrix(m, addMatrix(m, A21, A22), B11);
			int[][] R = strassenMultiplyMatrix(m, A11, subMatrix(m, B12, B22));
			int[][] S = strassenMultiplyMatrix(m, A22, subMatrix(m, B21, B11));
			int[][] T = strassenMultiplyMatrix(m, addMatrix(m, A11, A12), B22);
			int[][] U = strassenMultiplyMatrix(m, subMatrix(m, A21, A11), addMatrix(m, B11, B12));
			int[][] V = strassenMultiplyMatrix(m, subMatrix(m, A12, A22), addMatrix(m, B21, B22));
			
			int[][] C11 = addMatrix(m, subMatrix(m, addMatrix(m, P, S), T), V);
			int[][] C12 = addMatrix(m, R, T);
			int[][] C21 = addMatrix(m, Q, S);
			int[][] C22 = addMatrix(m, subMatrix(m, addMatrix(m, P, R), Q), U);
		
			for (int x = 0; x < m; x++)
			{
				for (int y = 0; y < m; y++)
				{
					C[x][y] = C11[x][y];
					C[x][y+m] = C12[x][y];
					C[x+m][y] = C21[x][y];
					C[x+m][y+m] = C22[x][y];
				}
			}
			return C;
		}
	}
	
	public static int[][] addMatrix(int n, int[][] A, int[][] B)
	{
		int[][] C = new int[n][n];
		for (int x = 0; x < n; x++)
			for (int y = 0; y < n; y++)
				C[x][y] = A[x][y] + B[x][y];	
		return C;
	}

	public static int[][] subMatrix(int n, int[][] A, int[][] B)
	{
		int[][] C = new int[n][n];
		for (int x = 0; x < n; x++)
			for (int y = 0; y < n; y++)
				C[x][y] = A[x][y] - B[x][y];

		return C;
	}
}
