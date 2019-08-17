

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.omg.CORBA.portable.InputStream;

/**
 * @author keshavgaddhyan
 * this is the class which makes the clients of the server
 * 
 * 
 * 
 * In this project i have completed everything, only the P2p model has not been implemented
 * The gui for client and server has been made and connections are done succesfully
 * my server is sending images to the clients 
 */
public class ImagePeer implements hash{
	String ip,pass,username;
	/**
	 * the arraylist actpeers gets the list of all active peers from the server
	 * 
	 * araylist bt stores the buttons recevied from the server to repaint
	 */
	public ArrayList<mypeers> actpeers=new ArrayList<>();
	ArrayList<Mybutton> bt=new ArrayList<Mybutton>();
	
	
	public static void main(String[] args) {
		
		
	ImagePeer x=new ImagePeer();
	x.login();
	x.setupnetwork();

	}
	
	/**
	 *  this functions sets up the connection between server and client
	 */
	void setupnetwork()
	{
		try {
			Socket sock=new Socket(ip,9000);
			PrintWriter writer=new PrintWriter(sock.getOutputStream());
			writer.println(username);
			writer.println(pass);
			writer.flush();	
			Thread t=new Thread(new clienthandler(sock));
			t.start();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 *  this function gets the user input of username and password for login
	 */
	void login()
	{
		 ip=getip();
		 username=getusr();
		 pass = null;
		try
		{
		 pass=hashing(getpass());
		}catch (Exception e) {
			System.out.println("Error in hashing the password");
			System.exit(0);
		}	
	}

	
	/**
	 * @return ip address of the server we want to connect to
	 * shows joption pane dialog box
	 */
	String getip()
	{

		 return JOptionPane.showInputDialog("Connect to Server");	
	}
	
	/**
	 * @returns usrername of the login user
	 */
	String getusr()
	{
		 return JOptionPane.showInputDialog("Username");
	}
	
	/**
	 * @return password of the lion user
	 */
	String getpass()
	{
		 return JOptionPane.showInputDialog("Password");
	}
	
	/* (non-Javadoc)
	 * @see hash#hashing(java.lang.String)
	 */
	public String hashing(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(password.getBytes());
        byte[] b = md.digest();
        StringBuffer temp = new StringBuffer();
        for(byte b1 : b){
            temp.append(Integer.toHexString(b1 & 0xff).toString());
        }
        return temp.toString();
	}
	
	/**
	 * @author keshavgaddhyan
	 * this class handles the clients and checks if connection is made successfully or not
	 * it also further makes a thread to share images
	 */
	class clienthandler implements Runnable
	{
		Socket s;
		public clienthandler(Socket sock) {
			s=sock;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void run() {

			
			try {
				
				//jclient.setSize(700,700);
				
				ObjectInputStream is = new ObjectInputStream(s.getInputStream());
				//InputStreamReader is=new InputStreamReader(s.getInputStream());
				//BufferedReader br=new BufferedReader(is);
				String dec= (String) is.readObject();
				if(dec.equals("false"))
				{
					JOptionPane.showMessageDialog(null, "Login Failed", "message",JOptionPane.ERROR_MESSAGE );
					System.exit(0);
				}
			



				System.out.println("connected");
				actpeers = (ArrayList<mypeers>) is.readObject();
				
				
				Thread t=new Thread(new getimageblock(s));
				t.start();
				
				
			   
			}

				
			 catch (Exception e) {
							e.printStackTrace();
			}
			
			 

			
			
		}	
	}
	
	/**
	 * @author keshavgaddhyan
	 * this class implements the thread to receive image blocks from the server and show in on the client gui
	 */
	class getimageblock implements Runnable
	{

		Socket p;
		public getimageblock(Socket m)
		{
			p=m;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
		 
			try {
				
				ObjectInputStream	nis = new ObjectInputStream(p.getInputStream());
				Mybutton x=(Mybutton) nis.readObject();
				while(x!=null)
				{
					bt.add(x);
					x=(Mybutton) nis.readObject();
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JFrame jclient= new JFrame("Image Peer");
			jclient.setSize(700,700);
			JPanel jp=new JPanel();
			jp=new JPanel();
			jp.setPreferredSize(new Dimension(700,700));
			jp.setLayout(new GridLayout(10,10));
			
			for(int i=0;i<bt.size();i++)
			{
				Mybutton btn=bt.get(i);
				jp.add(btn);
				
			}
			jclient.getContentPane().add(BorderLayout.CENTER,jp);
			jclient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jclient.setVisible(true);
			
		}
		
	}
}
	








