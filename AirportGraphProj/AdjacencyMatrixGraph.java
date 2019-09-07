import java.util.HashMap;
import java.util.LinkedList;

public class AdjacencyMatrixGraph
{
	public class Vertex
	{
		private String code;
		private String location;
		private int index;
		
		public Vertex(String v, String loc, int i)
		{
			code = v;
			location = loc;
			index = i;
		}
		
		public String getCode()
		{
			return code;
		}
		
		public String getLocation()
		{
			return location;
		}
		
		public int getIndex()
		{
			return index;
		}
		
		private void setIndex(int i)
		{
			index = i;
		}
		
	}

	public class Edge
	{
		private Vertex source;
		private Vertex destination;
		private double weight;
		
		Edge(Vertex s, Vertex d, double w)
		{
			source = s;
			destination = d;
			weight = w;
		}
		
		public Vertex getSource()
		{
			return source;
		}
		
		public Vertex getDestination()
		{
			return destination;
		}
		
		public double getWeight()
		{
			return weight;
		}
		
		private void setWeight(double w)
		{
			weight = w;	
		}
	}
	
	private int n;
	private int m;
	private LinkedList<Vertex> vertices;
	private LinkedList<Edge> edges;
	private AdjacencyMatrixGraph.Edge[][] matrix;
	private HashMap<Vertex, Integer> vertexToIndex;
	private HashMap<Integer, Vertex> indexToVertex;
	private HashMap<String, Vertex> codeToVertex;
	private boolean isDirected;
	
	AdjacencyMatrixGraph(boolean directed)
	{
		n = 0;
		m = 0;
		vertices = new LinkedList<Vertex>();
		edges = new LinkedList<Edge>();
		vertexToIndex  = new HashMap<>();
		indexToVertex = new HashMap<>();
		codeToVertex = new HashMap<>();
		isDirected = directed;
	}
	
	public int numVertices() 
	{
		return n;
	}
	
	public int numEdges()
	{
		return m;
	}
	
	public LinkedList<Vertex> vertices()
	{
		return vertices;
	}
	
	public LinkedList<Edge> edges()
	{
		return edges;
	}
	
	public Edge getEdge(Vertex u, Vertex v)
	{
		if (u != null && v != null)
			return matrix[u.getIndex()][v.getIndex()];
		else
			return null;
	}
	
	public Vertex[] endVertices(Edge e)
	{
		Vertex[] endpoints = new Vertex[2];
		endpoints[0] = e.getSource();
		endpoints[1] = e.getDestination();
		return endpoints;
	}
	
	public Vertex opposite(Vertex v, Edge e)
	{
		if (v == e.getSource())
			return e.getDestination();
		else
			return e.getSource();
	}
	
	public int outDegree(Vertex v)
	{
		int degree = 0;
		for (int x = 0; x < n; x++) 
			if (matrix[v.getIndex()][x] != null
				&& x != v.getIndex())
				degree++;
		return degree;
	}
	
	public int inDegree(Vertex v)
	{
		int degree = 0;
		for (int x = 0; x < n; x++)
			if (matrix[x][v.getIndex()] != null
				&& x != v.getIndex())
				degree++;
		return degree;
	}
	
	public LinkedList<Edge> outgoingEdges(Vertex v)
	{
		LinkedList<Edge> edges = new LinkedList<>();
		for (int x = 0; x < n; x++)
			if (matrix[v.getIndex()][x] != null
			&& x != v.getIndex())
				edges.addLast(matrix[v.getIndex()][x]);
		return edges;
	}
	
	public LinkedList<Edge> incomingEdges(Vertex v)
	{
		LinkedList<Edge> edges = new LinkedList<>();
		for (int x = 0; x < n; x++)
			if (matrix[x][v.getIndex()] != null
			&& x != v.getIndex())
				edges.addLast(matrix[x][v.getIndex()]);
		return edges;
	}
	
	public void insertVertex(String v, String loc, int i)
	{
		if (!validVertex(v))
		{
			Vertex temp = new Vertex(v, loc, i);
			vertexToIndex.put(temp, n);
			indexToVertex.put(n, temp);
			codeToVertex.put(v, temp);
			n++;
			vertices.addLast(temp);
			
			if (n == 1)
				matrix = new Edge[n][n];
			else
			{
				Edge[][] newMatrix = new Edge[n][n];
				for (int x = 0; x < n - 1; x++)
					for (int y = 0; y < n - 1; y++)
						newMatrix[x][y] = matrix[x][y];
				matrix = newMatrix;
			}
		}
	}
	
	public boolean insertEdge(Vertex u, Vertex v, double x)
	{
		if (u == null || v == null)
			return false;
		Edge e = getEdge(u, v);
		if (e == null)
		{
			e = new Edge(u, v, x);
			m++;
			edges.add(e);
			if (vertexToIndex.get(u) != null &&
				vertexToIndex.get(v) != null)
			{
				matrix[u.getIndex()][v.getIndex()] = e;
				if (!isDirected)
					matrix[v.getIndex()][u.getIndex()] = e;
			}
		}
		else
		{
			e.setWeight(x);
			if (!isDirected)
				matrix[v.getIndex()][u.getIndex()] = e;
		}
		return true;
	}	
	
	public void removeVertex(Vertex u)
	{
		if (u == null || u != vertices.get(u.getIndex()))
			return;
		else
		{
			for (Edge e : incomingEdges(u))
				removeEdge(e);
			for (Edge e : outgoingEdges(u))
				removeEdge(e);
			vertexToIndex.remove(u);
			indexToVertex.remove(u.getIndex());
			vertices.remove(u.getIndex());
			Edge[][] newMatrix = new Edge[n-1][n-1];
			for (int x = 0; x < n - 1; x++)
			{
				for (int y = 0; y < n - 1; y++)
				{
					if (x < u.getIndex() && y < u.getIndex())
						newMatrix[x][y] = matrix[x][y];
					else if (x < u.getIndex() && y > u.getIndex())
						newMatrix[x][y] = matrix[x][y+1];
					else if (x > u.getIndex() && y < u.getIndex())
						newMatrix[x][y] = matrix[x+1][y];
					else
						newMatrix[x][y] = matrix[x+1][y+1];
				}
			}
			for (int x = u.getIndex(); x < n - 1; x++)
				vertices.get(x).setIndex(x);
			matrix = newMatrix;
		}
	}
	
	public boolean removeEdge(Edge e)
	{
		if (e != null && codeToVertex(e.getSource().getCode()) == e.getSource()
				&& codeToVertex(e.getDestination().getCode()) == e.getDestination())
		{
			edges.remove(matrix[e.getSource().getIndex()][e.getDestination().getIndex()]);
			matrix[e.getSource().getIndex()][e.getDestination().getIndex()] = null;
			return true;
		}
		else
			return false;
	}
	
	private boolean validVertex(String v)
	{
		return codeToVertex.get(v) != null;
	}
	
	public String indexToCode(int i)
	{
		return indexToVertex.get(i).getCode();
	}
	
	public Vertex codeToVertex(String c)
	{
		return codeToVertex.get(c);
	}
	
	public boolean validIndex(int i)
	{
		return i >= 0 && i < numVertices();
	}
}
