package application;

import java.util.*;

/**
 * Class used for login authentication and adding new accounts
 * 
 * @author Hubert Borkowski
 *
 */
public class LoginLogic
{
    private List<Account> development;
    private List<Account> testing;
    private List<Account> production;
    private DataHandler dataHandler;

    /**
     * 
     * @param dh
     *            DataHandler object used with this LoginLogic object.
     */
    LoginLogic(DataHandler dh)
    {
	this.dataHandler = dh;
	this.development = new ArrayList<Account>();
	this.testing = new ArrayList<Account>();
	this.production = new ArrayList<Account>();
    }

    /**
     * Checks if given account is in database.
     * 
     * @param acc
     *            account to check
     * @param key
     *            name of department
     * @return true if acc in in database named by key, false otherwise
     */
    public Boolean isInData(Account acc, String key)
    {
	Boolean result = false;

	if (key.equals("development"))
	{
	    result = development.contains(acc);
	} else if (key.equals("testing"))
	{
	    result = testing.contains(acc);
	} else
	{
	    result = production.contains(acc);
	}

	return result;
    }

    /**
     * Receives and loads account data from dataHandler (assumes not-ordered data
     * input).
     */
    public void loadData()
    {
	dataHandler.read();
	List<Account> data = dataHandler.getData();

	for (Account acc : data)
	{
	    if (acc.getEnvironment().equals("development"))
	    {
		development.add(acc);
	    } else if (acc.getEnvironment().equals("testing"))
	    {
		testing.add(acc);
	    } else
	    {
		production.add(acc);
	    }
	}
    }

    /**
     * Returns List of usernames from selected environment. Omits first username for
     * testing purposes.
     * 
     * @param key
     *            selected environment name
     * @return List of usernames from selected environment
     */
    public List<String> getUserList(String key)
    {
	List<String> result = new ArrayList<String>();

	if (key.equals("development"))
	{
	    for (Account acc : development)
	    {
		result.add(acc.getUsername());
	    }
	    if (!result.isEmpty())
		result.remove(0);
	    return result;
	} else if (key.equals("testing"))
	{
	    for (Account acc : testing)
	    {
		result.add(acc.getUsername());
	    }
	    if (!result.isEmpty())
		result.remove(0);
	    return result;
	} else
	{
	    for (Account acc : production)
	    {
		result.add(acc.getUsername());
	    }
	    if (!result.isEmpty())
		result.remove(0);
	    return result;
	}
    }
}
