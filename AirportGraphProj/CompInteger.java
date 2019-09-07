import java.util.Comparator;

public class CompInteger implements Comparator<Entry<Integer, String>>
{

	public int compare(Entry<Integer, String> e1, Entry<Integer, String> e2) 
	{
		if ((int)e1.getKey() < e2.getKey())
		{
			return -1;
		}
		else if ((int)e1.getKey() == e2.getKey())
		{
			return 0;
		}
		else
			return 1;
	}

}
