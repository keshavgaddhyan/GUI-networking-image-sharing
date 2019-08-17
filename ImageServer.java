import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * @author keshavgaddhyan
 * this class is the server for my programme
 * all the functions of hashing and image loading are implemented inside
 */
public class ImageServer {

	
	JFrame jf;
	JPanel jp;
	JButton jb;
	/**
	 * buttons store the image bloacks of the loaded image
	 *  
	 *  currpeers store the information of the current active peers
	 */
	ArrayList<Mybutton> buttons;
	ArrayList<records> store=new ArrayList<>();
	ArrayList<mypeers> currpeers=new ArrayList<>();
	
	
	public static void main(String args[])
	{
		ImageServer a=new ImageServer();
		
		a.go();
	}
	
	/**
	 *  initiates the programme and starts a thread for each new peer added
	 *  it also makes the main gui of the server and adds the change image button
	 */
	public void go()
	{
		 jf=new JFrame("Image Server");
		 jb=new JButton("Load another image");
		jb.addActionListener(new loadListen());
		 try
		 {
			 setimg();
		 }catch (Exception e) {
			 System.out.println("error in loading image");
			System.exit(0);
		} 
		read();

	        
		try
		{
			ServerSocket ss=new ServerSocket(9000);
			while(true)
			{
				Socket s=ss.accept();
				
				Thread many=new Thread(new clients(s));
				many.start();
				
				
			}
			
		}catch (Exception e) {
			System.out.println("error is establishng connection");
		}
	}
	
	
	/**
	 * @throws Exception
	 * 
	 * 
	 * this function takes the image input using file reader and then divides it into 100 buttons
	 */
	 
	public void setimg() throws Exception
	{
		String fpath=filechooser();
		 ImageIcon y=resizeing(fpath);
		 BufferedImage bi = new BufferedImage(
				    y.getIconWidth(),
				    y.getIconHeight(),
				    BufferedImage.TYPE_INT_RGB);
				Graphics g = bi.createGraphics();
				y.paintIcon(null, g, 0,0);
				g.dispose();
		buttons=new ArrayList<>();
		jp=new JPanel();
		jp.setPreferredSize(new Dimension(700,700));
		jp.setLayout(new GridLayout(10,10));
		
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				BufferedImage im =bi.getSubimage(j*70,i*70, 70, 70);
				Image image=(Image)im;
				Mybutton button=new Mybutton(image);
				button.setx(i);
				button.sety(j);
				buttons.add(button);
			}
	}

		showimage(buttons);
		jf.getContentPane().add(BorderLayout.SOUTH,jb);
		jf.getContentPane().add(BorderLayout.CENTER,jp);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		
	}
	
	
	/**
	 *@param temp is the list of buttons required to display
	 * this function displays the buttons which form the image on the panel p
	 */
	void showimage(ArrayList <Mybutton> temp)
	{
		for(int i=0;i<100;i++)
		{
			Mybutton btn=temp.get(i);
			jp.add(btn);
			
		}

		jf.getContentPane().add(BorderLayout.NORTH,jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
	
	
	/**
	 * @return path of the file chosen
	 * @throws Exception
	 * opens a dialog box so that the user can choose the required image
	 */
	String filechooser() throws Exception
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
		File selectedFile = jfc.getSelectedFile();
		 String t= selectedFile.getAbsolutePath();
     return t;
	    }
		else if(returnValue == JFileChooser.CANCEL_OPTION)
		{
            System.out.println("File not Selected");
		return null;
		}
		else
		{
		System.out.println("Faild to load image");
		return null;
		}
			
    }
	
	/**
	 * @param x is the path of the image
	 * @return imageicon of the resized image
	 * @throws Exception
	 */
	public  ImageIcon resizeing(String x) throws Exception
	  {
   ImageIcon MyImage = new ImageIcon(x);
	  Image org = MyImage.getImage();
	  Image newImg = org.getScaledInstance(700, 700, Image.SCALE_SMOOTH);
	  ImageIcon image = new ImageIcon(newImg);
	  return image;
	}
	
	/**
	 *  it reads the JSON file User.txt and stores the details in an array list records
	 */
	public void read() 
	{
		try {
		File f=new File("User.txt");
		if(f.exists())
		{
		JSONParser parser=new JSONParser();
		JSONObject obj=(JSONObject)parser.parse(new FileReader("User.txt"));
		JSONArray xyz= (JSONArray) obj.get("user_array");
		
		for(int i=0;i<xyz.size();i++)
		{
			JSONObject n= (JSONObject) xyz.get(i);
			records temp=new records();
			temp.username= n.get("username").toString();
			temp.pass=n.get("hash_password").toString();
			store.add(temp);
		}
		
	}
		}
		catch (Exception e) {
			System.out.println("Unanle to read the JSON object file");
		}
	
	}
	
	/**
	 * @author keshavgaddhyan
	 * this class implements the listener for the load another image button
	 */
	public class loadListen implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try
			{
				setimg();
			}catch (Exception e1) {
				System.out.println("error loading new image");
			}


		}
	}
	
	/**
	 * @author keshavgaddhyan
	 * this class stores the details extracted from user.txt
	 */
	class records
	{
		String username="";
		String pass="";
	}
	
	
	
	/**
	 * @author keshavgaddhyan
	 * this class reads the input from the client and checks if they match the records
	 *  it also sends the image blocks to the client programme
	 */
	class clients implements Runnable{

		Socket a;
		public clients(Socket s) {
			a=s;
		}

		@Override
		public void run() {
			try {
			String connect="false";
			InputStreamReader is=new InputStreamReader(a.getInputStream());
			BufferedReader br=new BufferedReader(is);
			String usname=br.readLine();
			String pass=br.readLine();
			for(int i=0;i<store.size();i++)
			{
				if(store.get(i).username.equals(usname) && store.get(i).pass.equals(pass))
				{
					connect="true";
				}
			}
			ObjectOutputStream objectOutput = new ObjectOutputStream(a.getOutputStream());
			//PrintWriter w=new PrintWriter(a.getOutputStream(),true);
			if(connect.equals("true"))
			{
			

				objectOutput.writeObject(connect);
				mypeers x=new mypeers(a.getInetAddress(),a.getPort()) ;
				currpeers.add(x);	
				
                objectOutput.writeObject(currpeers);  
                
                ObjectOutputStream sendimg = new ObjectOutputStream(a.getOutputStream());
                for(int i=0;i<buttons.size();i++)
                {
                	sendimg.writeObject(buttons.get(i));
                	sendimg.flush();
                	
                	
                }
                sendimg.writeObject(null);
                sendimg.flush();
			}
             
			else
			{
				
				objectOutput.writeObject(connect);
			}
			}catch (Exception e) {
				System.out.println("Unable to establish login connection");
				e.printStackTrace();
			}
		}
	}
}
			
	
	

