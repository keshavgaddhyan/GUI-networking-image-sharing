

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author keshavgaddhyan
 * this class is an extension of jbutton 
 * these buttons stores the imageIcon of each block of the whole image
 */
@SuppressWarnings("serial")
public class Mybutton extends JButton implements Serializable {

	int x ,y;
	
	/**
	 *  parent constructor
	 */
	public Mybutton()
	{
		super();
	}
	
	/**
	 * @param image to assign the button the image
	 */
	public Mybutton(Image image) {
		super(new ImageIcon(image));
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param a to set x location of button
	 */
	void setx(int a)
	{
		x=a;
	}
	 /**
	 * @param b is to set y location of button
	 */
	void sety(int b)
	 {
		 y=b;
	 }
	 
	 /**
	 * @return x location of button
	 */
	int getx()
	 {
		 return x;
		 
	 }
	 
	 /**
	 * @return y location of button
	 */
	int gety()
	 {
		 return y;
	 }
	
	
	
}