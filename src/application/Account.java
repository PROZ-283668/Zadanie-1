package application;

/**
 * Container for account data. Holds username, account password and the
 * asociated work environment.
 * 
 * @author Hubert Borkowski
 */

public class Account
{
    private String username;
    private String password;
    private String environment;

    /**
     * Account class constructor. Non-parametric constructor not needed for this
     * class.
     * 
     * @param username
     *            account username
     * @param password
     *            account password
     * @param environment
     *            working environment associated with account
     */
    Account(String username, String password, String environment)
    {
	this.username = username;
	this.password = password;
	this.environment = environment;
    }

    @Override
    public int hashCode()
    {
	int result = 17;

	result = 31 * result + username.hashCode();
	result = 31 * result + password.hashCode();
	result = 31 * result + environment.hashCode();

	return result;
    }

    /**
     * Function overridden to allow different objects with equal fields to be
     * considered equal.
     */
    @Override
    public boolean equals(Object other)
    {
	if (other == null)
	    return false;
	if (other == this)
	    return true;
	if (!(other instanceof Account))
	    return false;

	Account otherAccount = (Account) other;
	return this.username.equals(otherAccount.getUsername()) && this.password.equals(otherAccount.getPassword())
		&& this.environment.equals(otherAccount.getEnvironment());
    }

    public String getUsername()
    {
	return username;
    }

    public String getPassword()
    {
	return password;
    }

    public String getEnvironment()
    {
	return environment;
    }
}
