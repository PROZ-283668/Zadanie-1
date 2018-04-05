package application;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class used for reading/writing the account database.
 * 
 * @author Hubert Borkowski
 *
 */
public class DataHandler
{
    private List<Account> data;
    private String fileName;

    /**
     * Sets the name of the data storage file. Data itself is either read from file
     * or passed from different class.
     */
    DataHandler()
    {
	this.fileName = "data.txt";
	this.data = new ArrayList<Account>();
    }

    /**
     * Reads data from file with name fileName
     */
    public void read()
    {
	try
	{
	    String[] buf = new String[3];

	    List<String> lines = Files.readAllLines(Paths.get(fileName));
	    for (String s : lines)
	    {
		buf = s.split(" ");
		data.add(new Account(buf[0], buf[1], buf[2]));
	    }

	} catch (Exception e)
	{
	    e.getStackTrace();
	}
    }

    public List<Account> getData()
    {
	return data;
    }

    public void setData(List<Account> data)
    {
	this.data = data;
    }
}
