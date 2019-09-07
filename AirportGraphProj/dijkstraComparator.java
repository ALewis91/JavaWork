import java.util.Comparator;
import javafx.util.Pair;

public class dijkstraComparator implements Comparator<Pair<AdjacencyMatrixGraph.Vertex, Double>>
{
	public int compare(Pair<AdjacencyMatrixGraph.Vertex, Double> o1, Pair<AdjacencyMatrixGraph.Vertex, Double> o2) 
	{
		if (o1.getValue() < o2.getValue())
			return -1;
		else if (o1.getValue() > o2.getValue())
			return 1;
		return 0;
	}

}
