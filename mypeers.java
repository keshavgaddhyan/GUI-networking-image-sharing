import java.io.Serializable;
import java.net.InetAddress;

/**
 * @author keshavgaddhyan
 * this class is used to share the ip address and port number from the server to all of its peers
 */
public class mypeers implements Serializable {
	int p;
	InetAddress add;


	/**
	 * @param add to assign ip address values to the private members
	 * @param p to assign port number to the private members
	 */
	public mypeers(InetAddress add, int p) {
		super();
		this.add = add;
		this.p = p;
	}

	/**
	 * @return ip address
	 */
	public InetAddress getadd() {
		return add;
	}

	/**
	 * @param add is the ip address to be set
	 */
	public void setadd(InetAddress add) {
		this.add = add;
	}

	/**
	 * @return port number of the client
	 */
	public int getp() {
		return p;
	}

	/**
	 * @param p is the port number to be set
	 */
	public void setp(int p) {
		this.p = p;
	}
}