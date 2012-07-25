package testCheater;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenKinectFrameGrabber;
import com.googlecode.javacv.cpp.freenect;
import com.googlecode.javacv.cpp.freenect.freenect_depth_cb;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.*;
//import com.sun.jna.Library;



public class Kin {

/*
	public static void main(String[] args) throws Exception {
		
		boolean goal=false;
		OpenKinectFrameGrabber grabber=new OpenKinectFrameGrabber(0);
		while(!goal){
			Pixel p=detectCheck(grabber);
			if(p.x<340&&p.x>300)
			{
				if(!goal){
					//move!
				}
				else{
					//move with IP
					//goal!
				}
			}
			else{
				if(p.x<300){
					//move right
				}
				else{
					//move left
				}
			}
		}
		//FrameGrabber grabber = FrameGrabber.createDefault(0);
		OpenKinectFrameGrabber grabber=new OpenKinectFrameGrabber(0);
		//grabber.setFormat("depth");
		
		grabber.start();
		IplImage grabbedImage = grabber.grab();
//		BufferedImage pic=grabbedImage.getBufferedImage();
		
//		int w=pic.getWidth();
//		int h=pic.getHeight();
//		int typ=pic.getType();
//		BufferedImage pic2=new BufferedImage(w, h, 5);

///*		rgbPoint[][] pixels=new rgbPoint[w][h];
//
//		for (int y=0; y < h; ++y)
//			for (int x=0; x < w; ++x)
//				pixels[x][y]= convert_ARGB_to_RGB(pic.getRGB(x, y));
//
//		for (int y=0; y < h; ++y)
//			for (int x=0; x < w; ++x)
//			{  
//				int t= (0xff000000 | pixels[x][y].r << 16) | (pixels[x][y].g<< 8) | pixels[x][y].b;
//				pic2.setRGB(x, y, t);
//			}
//
//		File outputfile = new File("saved.jpg");
//		ImageIO.write(pic2, "jpg", outputfile);
		


		CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());
		
		while (frame.isVisible() && (grabbedImage = grabber.grab()) != null) {
			//System.out.println(detectGranier(grabbedImage));  
			//******ShortBuffer testbut=grabbedImage.getShortBuffer();
			//int x=grabbedImage.width()/2;
			//int y= grabbedImage.height()/2;
			//ByteBuffer testbut=grabbedImage.getByteBuffer();
			//short d=testbut.get(x * 2 + y * grabbedImage.width() * 2+1);
			//d = (short) (d << 8);
			//d=(short) (d+testbut.getInt(x * 2 + y * grabbedImage.width() * 2));
			//byte a=testbut.get();
			//byte b=testbut.get();
		    //b=testbut1.get();
		    //short temp=testbut1.get();
			//System.out.println(testbut.limit());
			//int x=(int) (a | b << 8);
			 //x=x+10000;
			//int x=(int)(a >> 3 | b << 5);
			//*******short y= (short) (testbut.get(306879));
			//System.out.println(grabbedImage.depth(y)); 
			//double y=testbut.get()&0xFFFF;
			//int y=testbut.get();
			//y=testbut.get();
			//grabbedImage.get
			//*****double depthM;
			
			//*****depthM= 0.1236 * Math.tan(y / 2842.5 + 1.1863);
			//depthM= 100/(-0.00307 * y + 3.33);
			
			//short a=testbut1.get();
			//short b=testbut1.get();
			//short x=(short)  (a | b << 8);
			//double depthM = 0.1236 * Math.tan(y / 2842.5 + 1.1863);
			Pixel p=detectCheck(grabber);
			if (p.x==-1)
				System.out.println("false");
			else
				System.out.println("true");
			frame.showImage(grabbedImage);
			//d=d/1000;
			//******System.out.println(depthM+ "::"+y);
		}
		
		frame.dispose();
		grabber.stop();
	}
*/

	public static Pixel detectGranier(IplImage image){
		BufferedImage bi=image.getBufferedImage();
        int w=bi.getWidth();
        int h=bi.getHeight();
        int count=0;
        int[] hight=new int[h];
        int[] width=new int[w];
        boolean ans=false;
        for (int y=0; y < h; ++y){
            for (int x=0; x < w; ++x){
				int pic=bi.getRGB(x, y);
		    	int r = (pic >> 16) & 0xFF;
		    	int g = (pic >> 8) & 0xFF;
		    	int b = (pic >> 0) & 0xFF;
		    	double D = Math.sqrt(Math.pow((r-0),2) + Math.pow((g-255),2) + Math.pow((b-0),2));
		    	if(D<162){
		    		hight[y]+=1;
		    		width[x]+=1;		    		
		    		ans= true;
		    		count++;
		    	}
            }
    	}
        //System.out.println("x="+getMax(width)+", y="+getMax(hight));
        if(!ans)
        	return new Pixel(-1, -1); //no green pixel in sight
        //System.out.println(count);
        if(count>=265){
        	System.out.println(count);
        	return new Pixel(-2,-2); //alot of green pixels
        }
        Pixel p=new Pixel(getMax(width),getMax(hight));
        return p;
	}
	
	public static Pixel detectCheck(OpenKinectFrameGrabber grabber) throws Exception{
         int count=0;
         Pixel p=new Pixel(-1, -1);
         for(int i=0;i<10;i++){
        	 	 p=detectGranier(grabber.grab());
                 if(p.x!=-1){
                	 count++;
                 }
                         
         }
         if(count/10>=0.5){
                return p; 
         }
         return new Pixel(-1, -1);
 }
		
	private static int getMax(int[] arr) {
		int max=arr[0];
		int index=0;
		for(int i=1;i<arr.length;i++){
			if(max<arr[i]){max=arr[i];index=i;}
		}
		return index;
		
	}
	
}