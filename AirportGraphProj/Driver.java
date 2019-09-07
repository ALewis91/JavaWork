import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import javafx.util.Pair;

import java.text.NumberFormat;

public class Driver {

	public static void main(String[] args) throws FileNotFoundException 
	{
		System.out.println("Author: Aaron Lewis\n");
		AdjacencyMatrixGraph AirNetwork = new AdjacencyMatrixGraph(true);
		Scanner fileScan = new Scanner(new File("P4Airports.txt"));
		buildAirports(AirNetwork, fileScan);
		fileScan = new Scanner(new File("P4Flights.txt"));
		buildFlights(AirNetwork, fileScan);
		fileScan.close();
		Scanner scan = new Scanner(System.in);
		boolean addedAirport = false;
		boolean changedFlights = false;
		
		boolean done = false;
		while (!done)
		{
			int selection = menuOptions(scan);
			switch(selection)
			{
			case 0: 
			{
				printGraph(AirNetwork);
				break;
			}
			case 1:
			{
				System.out.print("\nEnter the airport code: " );
				String code = scan.nextLine();
				System.out.println();
				displayAirportInfo(AirNetwork, code);
				System.out.println();
				break;
			}
			case 2:
			{
				String source;
				String destination;
				System.out.print("\nEnter the source airport code: " );
				source = scan.nextLine();
				System.out.print("Enter the destination airport code: " );
				destination = scan.nextLine();
				findCheapestFlight(AirNetwork, source, destination);
				
				break;
			}
			case 3:
			{
				String source;
				String destination;
				double cost;
				System.out.print("\nEnter the source airport code: " );
				source = scan.nextLine();
				System.out.print("Enter the destination airport code: " );
				destination = scan.nextLine();
				System.out.print("Enter the cost: " );
				cost = scan.nextDouble();
				scan.nextLine();
				if(AirNetwork.insertEdge(AirNetwork.codeToVertex(source),
										 AirNetwork.codeToVertex(destination),
										 cost))
				{
					System.out.println("Flight added!\n");
					changedFlights = true;
				}
				else
					System.out.println("Invalid code(s)! Flight could not be added.\n");
										 
				break;
			}
			case 4:
			{
				String source;
				String destination;
				System.out.print("\nEnter the source airport code: " );
				source = scan.nextLine();
				System.out.print("Enter the destination airport code: " );
				destination = scan.nextLine();
				AdjacencyMatrixGraph.Edge e;
				e = AirNetwork.getEdge(AirNetwork.codeToVertex(source), AirNetwork.codeToVertex(destination));
				if (AirNetwork.removeEdge(e))
				{
					System.out.println("Flight from " + source + " to " + destination + " removed.\n");
					changedFlights = true;
				}
				else
					System.out.println("Flight from " + source + " to " + destination + " does not exist!\n");				
				break;
			}
			case 5:
			{
				String source;
				String destination;
				System.out.print("\nEnter the source airport code: " );
				source = scan.nextLine();
				System.out.print("Enter the destination airport code: " );
				destination = scan.nextLine();
				findCheapestRoundTrip(AirNetwork, source, destination);			
				break;
			}
			case 6:
			{
				String source;
				String destination;
				System.out.print("\nEnter the source airport code: " );
				source = scan.nextLine();
				System.out.print("Enter the destination airport code: " );
				destination = scan.nextLine();
				fewestStops(AirNetwork, source, destination);
				break;
			}
			case 7:
			{
				String code;
				String location;
				System.out.print("\nEnter the new airport code: " );
				code = scan.nextLine();
				System.out.print("Enter the new airport location: " );
				location = scan.nextLine();
				AirNetwork.insertVertex(code, location, AirNetwork.numVertices());
				addedAirport = true;
				System.out.println("Airport added!\n");
				break;
			}
			case 10:
			{
				done = true;
				if (addedAirport)
				{
					PrintWriter pw = new PrintWriter(new File("P4AirportssRev1.txt"));
					writeAirports(AirNetwork, pw);
				}
				if (changedFlights)
				{
					PrintWriter pw = new PrintWriter(new File("P4FlightsRev1.txt"));
					writeFlights(AirNetwork, pw);
				}
			}
			}
			
		}
	}
	
	public static int menuOptions(Scanner scan)
	{
		String selection = "";
		System.out.println("1. Display airport information");
		System.out.println("2. Find the cheapest flight from one airport to another airport");
		System.out.println("3. Add a flight from one airport to another airport");
		System.out.println("4. Delete a flight from one airport to another airport");
		System.out.println("5. Find the cheapest roundtrip from one airport to another airport");
		System.out.println("6. Find a flight with fewest stops from one airport to another airport");
		System.out.println("7. Add a new airport");
		System.out.println("Q. Exit");
		
		selection = scan.nextLine();
		while (!validMenuOption(selection))
		{
			System.out.println("Please selection an option from the menu!");
			selection = scan.nextLine();
		}
		if (selection.equals("Q"))
			return 10;
		else
			return Integer.parseInt(selection);	
	}
	
	public static boolean validMenuOption(String s)
	{
		try
		{
			int selection = Integer.parseInt(s);
			if (selection >= 0 && selection <= 7)
				return true;
			else
				return false;
		}
		catch (NumberFormatException e)
		{
			if (!s.equals("Q"))
				return false;
			else
				return true;
		}
		
	}
	public static void displayAirportInfo(AdjacencyMatrixGraph g, String code)
	{
		if (g.codeToVertex(code) != null)
		{
			NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
			AdjacencyMatrixGraph.Vertex airport = g.codeToVertex(code);
			System.out.println(" Code:     " + airport.getCode());
			System.out.println(" Location: " + airport.getLocation());
			
			if (g.outDegree(airport) > 0)
			{
				System.out.println(" Provides flights to:");
				System.out.println("  Code      Location       Price");
				for (AdjacencyMatrixGraph.Edge e : g.outgoingEdges(airport))
				{
					AdjacencyMatrixGraph.Vertex v = g.opposite(airport, e);
					System.out.print("  " + v.getCode());
					System.out.printf("%15s", v.getLocation());
					System.out.printf("%12s", moneyFormat.format(e.getWeight()));
					System.out.println();
				}
			}
		}
		else
			System.out.println("That airport does not exist.");
	}
	
	public static void findCheapestFlight(AdjacencyMatrixGraph g, String src, String dst)
	{
		NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
		LinkedList<AdjacencyMatrixGraph.Vertex> route;
		route = cheapestRoute(g, src, dst);
		if (route != null)
		{
			for (int x = 0; x < route.size() - 1; x++)
				System.out.print(route.get(x).getCode() + "->");
			System.out.println(route.getLast().getCode());
			System.out.println("Cost: " + moneyFormat.format(getCost(g, route)) + "\n");
		}
	}
	
	public static void findCheapestRoundTrip(AdjacencyMatrixGraph g, String src, String dst)
	{
		NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
		LinkedList<AdjacencyMatrixGraph.Vertex> toRoute;
		LinkedList<AdjacencyMatrixGraph.Vertex> fromRoute;
		toRoute = cheapestRoute(g, src, dst);
		fromRoute = cheapestRoute(g, dst, src);
		if (toRoute != null && fromRoute != null)
		{
			for (int x = 0; x < toRoute.size() - 1; x++)
				System.out.print(toRoute.get(x).getCode() + "->");
			System.out.println(toRoute.getLast().getCode());
			for (int x = 0; x < fromRoute.size() - 1; x++)
				System.out.print(fromRoute.get(x).getCode() + "->");
			System.out.println(fromRoute.getLast().getCode());
			System.out.print("Cost: ");
			System.out.println(moneyFormat.format(getCost(g, toRoute) + getCost(g, fromRoute)) + "\n");
		}
	}
	public static LinkedList<AdjacencyMatrixGraph.Vertex> cheapestRoute(AdjacencyMatrixGraph g, String src, String dst)
	{
		if (g.codeToVertex(src) == null || g.codeToVertex(dst) == null)
		{
				System.out.println("That airport does not exist!\n");
				return null;
		}
		else
		{
			int n = g.numVertices();
			double[] D = new double[n];
			HashMap<AdjacencyMatrixGraph.Vertex, AdjacencyMatrixGraph.Vertex> path = new HashMap<>();
			for (AdjacencyMatrixGraph.Vertex v : g.vertices())
			{
				if (v == g.codeToVertex(src))
					D[g.codeToVertex(src).getIndex()] = 0;
				else
					D[v.getIndex()] = Double.MAX_VALUE;
					
			}
			
			dijkstraComparator c = new dijkstraComparator();
			PriorityQueue<Pair<AdjacencyMatrixGraph.Vertex, Double>> pq = new PriorityQueue<>(1, c);
			HashMap<AdjacencyMatrixGraph.Vertex, Pair<AdjacencyMatrixGraph.Vertex, Double>> container = new HashMap<>();
			
			for (AdjacencyMatrixGraph.Vertex v : g.vertices())
			{
				Pair<AdjacencyMatrixGraph.Vertex, Double> temp = new Pair<AdjacencyMatrixGraph.Vertex, Double>(v, D[v.getIndex()]);
				pq.add(temp);
				container.put(temp.getKey(), temp);
			}
			
			while (!pq.isEmpty())
			{
				Pair<AdjacencyMatrixGraph.Vertex, Double> u = pq.poll();
				for (AdjacencyMatrixGraph.Edge e : g.outgoingEdges(u.getKey()))
				{
					AdjacencyMatrixGraph.Vertex opposite = g.opposite(u.getKey(),e);
					if (D[u.getKey().getIndex()] + e.getWeight() < D[opposite.getIndex()])
					{
						D[opposite.getIndex()] = D[u.getKey().getIndex()] + e.getWeight();
						pq.remove(container.get(opposite));
						Pair<AdjacencyMatrixGraph.Vertex, Double> temp = new Pair<AdjacencyMatrixGraph.Vertex, Double>(opposite, D[opposite.getIndex()]);
						container.put(temp.getKey(), temp);
						pq.add(container.get(temp.getKey()));
						path.put(temp.getKey(), u.getKey());
					}
				}
					
			}
			LinkedList<AdjacencyMatrixGraph.Vertex> stops = new LinkedList<>();
			AdjacencyMatrixGraph.Vertex current = g.codeToVertex(dst);
			while (current != g.codeToVertex(src))
			{
				stops.addFirst(current);
				current = path.get(current);
			}
			stops.addFirst(current);
			return stops;
		}
	}

    static double getCost(AdjacencyMatrixGraph g, LinkedList<AdjacencyMatrixGraph.Vertex> path)
	{
		AdjacencyMatrixGraph.Vertex source;
		AdjacencyMatrixGraph.Vertex destination;
		double total = 0;
		for (int x = 0; x < path.size() - 1; x++)
		{
			source = path.get(x);
			destination = path.get(x+1);
			total += g.getEdge(source, destination).getWeight();
		}
		return total;
	}
	public static void buildAirports(AdjacencyMatrixGraph g, Scanner scan)
	{
		while (scan.hasNext())
		{
			int index = scan.nextInt();
			String code = scan.next();
			String location = scan.nextLine();
			int startIndex = 0;
			while (location.charAt(startIndex) == ' ')
				startIndex++;
			location = location.substring(startIndex, location.length());
			g.insertVertex(code, location, index);
		}
	}
	
	public static void buildFlights(AdjacencyMatrixGraph g, Scanner scan)
	{
		int source, destination;
		AdjacencyMatrixGraph.Vertex s, d;
		double cost;
		while (scan.hasNext())
		{
			source = scan.nextInt();
			destination = scan.nextInt();
			cost = scan.nextDouble();

			if (g.validIndex(source) && g.validIndex(destination) &&
				source != destination)
			{
				s = g.codeToVertex(g.indexToCode(source));
				d = g.codeToVertex(g.indexToCode(destination));
				g.insertEdge(s, d, cost);
			}
	
		}
	}
	
	public static void fewestStops(AdjacencyMatrixGraph g, String src, String dst)
	{
		NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
		AdjacencyMatrixGraph.Vertex source = g.codeToVertex(src);
		AdjacencyMatrixGraph.Vertex destination = g.codeToVertex(dst);
		if (source == null || destination == null)
		{
			if (source == null && destination == null)
				System.out.println(src + " and " + destination + " are not valid airport codes!");
			else if (source == null)
				System.out.println(src + " is not a valid airport code!");
			else
				System.out.println(dst + " is not a valid airport code!");
			return;
		}
		
		HashMap<AdjacencyMatrixGraph.Vertex, AdjacencyMatrixGraph.Vertex> known = new HashMap<>();
		known.put(source, source);
		Queue<AdjacencyMatrixGraph.Vertex> q = new LinkedList<AdjacencyMatrixGraph.Vertex>();
		q.add(source);
		while (known.size() < g.numVertices())
		{
			AdjacencyMatrixGraph.Vertex current = q.poll();
			for (AdjacencyMatrixGraph.Edge e : g.outgoingEdges(current))
			{
				AdjacencyMatrixGraph.Vertex next = g.opposite(current, e);
				if (known.get(next) == null)
				{
					known.put(next, current);
					q.add(next);
				}
			}
		}
		AdjacencyMatrixGraph.Vertex current = destination;
		LinkedList<AdjacencyMatrixGraph.Vertex> route = new LinkedList<>();
		while (current != source)
		{
			route.addFirst(current);
			current = known.get(current);
		}
		route.addFirst(current);
		for (int x = 0; x < route.size() - 1; x++)
			System.out.print(route.get(x).getCode() + "->");
		System.out.println(route.getLast().getCode());
		System.out.println("Cost: " + moneyFormat.format(getCost(g, route)) + "\n");
	}
	


	public static void printGraph(AdjacencyMatrixGraph g)
	{
		NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
		System.out.println("\nVertices:");
		for (AdjacencyMatrixGraph.Vertex v : g.vertices())
		{
			System.out.printf("%2d", v.getIndex());
			System.out.println(" " + v.getCode() + " " + v.getLocation());
		
		}
			System.out.println("\nEdges:");
		for (AdjacencyMatrixGraph.Edge e : g.edges())
		{
			System.out.printf("%2d", g.edges().indexOf(e));
			System.out.print(" " + e.getSource().getCode());
			System.out.print(" -> " + e.getDestination().getCode());
			System.out.printf("%9s", moneyFormat.format(e.getWeight()));
			System.out.println();
		}
		System.out.println();
	}
	
	public static void test(AdjacencyMatrixGraph g)
	{
		System.out.println("Function test of Graph");
		System.out.println("\nnumVertices: " + g.numVertices());
		System.out.println("\nnumEdges: " + g.numEdges() + "\n");
		printGraph(g);
		System.out.println("\nEdge LAX -> SEA: ");
		AdjacencyMatrixGraph.Edge e = g.getEdge(g.codeToVertex("LAX"), g.codeToVertex("SEA"));
		System.out.println(" Source: " + e.getSource().getCode());
		System.out.println(" Destination: " + e.getDestination().getCode());
		System.out.println(" Weight: " + e.getWeight());
		System.out.println("\nEdge SEA -> ORD: ");
		e = g.getEdge(g.codeToVertex("SEA"), g.codeToVertex("ORD"));
		System.out.println(" Source: " + e.getSource().getCode());
		System.out.println(" Destination: " + e.getDestination().getCode());
		System.out.println(" Weight: " + e.getWeight());
		System.out.println("\nEnd Vertices of edge SEA->ORD: ");
		for (AdjacencyMatrixGraph.Vertex v : g.endVertices(e))
			System.out.println(" " + v.getCode());
		System.out.print("\nOpposite of " + e.getSource().getCode() + ": ");
		System.out.println(g.opposite(e.getSource(), e).getCode());
		System.out.println("\nOutdegree of JFK: " + g.outDegree(g.codeToVertex("JFK")));
		System.out.println("\nIndegree of JFK: " + g.inDegree(g.codeToVertex("JFK")));
		System.out.println("\nOutgoing edges of JFK: ");
		for (AdjacencyMatrixGraph.Edge d : g.outgoingEdges(g.codeToVertex("JFK")))
		{
			System.out.print(" " + d.getSource().getCode() + "->");
			System.out.print(d.getDestination().getCode());
			System.out.println(" " + d.getWeight());
		}
		System.out.println("\nIncoming edges of JFK: ");
		for (AdjacencyMatrixGraph.Edge d : g.incomingEdges(g.codeToVertex("JFK")))
		{
			System.out.print(" " + d.getSource().getCode() + "->");
			System.out.print(d.getDestination().getCode());
			System.out.println(" " + d.getWeight());
		}
		System.out.println("\nRemoving Vertex BOS\n");
		g.removeVertex(g.codeToVertex("BOS"));
		printGraph(g);
		System.out.println("\nRemoving Edge JFK->MSY\n");
		e = g.getEdge(g.codeToVertex("JFK"),  g.codeToVertex("MSY"));
		g.removeEdge(e);
		printGraph(g);
	}
	
	public static void writeAirports(AdjacencyMatrixGraph g, PrintWriter pw)
	{
		for (AdjacencyMatrixGraph.Vertex v : g.vertices())
			pw.println(v.getIndex() + "  " + v.getCode() + "    " + v.getLocation());
		pw.close();
	}
	
	public static void writeFlights(AdjacencyMatrixGraph g, PrintWriter pw)
	{
		for (AdjacencyMatrixGraph.Edge e : g.edges())
		{
			pw.print(e.getSource().getIndex() + "   " + e.getDestination().getIndex());
			pw.printf("%10.2f", e.getWeight());
			pw.println();
		}
		pw.close();
	}
}
