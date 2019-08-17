import java.security.NoSuchAlgorithmException;

import java.security.NoSuchAlgorithmException;

/**
 * @author keshavgaddhyan
 *This is the Interface class of hash and the function hashing is implemented in the user class
 */
public interface hash {

	/**
	 * @param pass is the password to be hashed
	 * @return the hashed value of password
	 * @throws NoSuchAlgorithmException
	 */
	public String hashing(String pass) throws NoSuchAlgorithmException;
}

