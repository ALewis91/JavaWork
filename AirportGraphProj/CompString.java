import java.util.Comparator;

public class CompString implements Comparator<Entry<String, Integer>>
{

	public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) 
	{
		return e1.getKey().compareTo(e2.getKey());
	}

}
