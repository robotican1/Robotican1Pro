package testCheater;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenKinectFrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.freenect;
import com.googlecode.javacv.cpp.freenect.freenect_depth_cb;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.IplImageArray;
import com.googlecode.javacv.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
//import static com.googlecode.javacv.cpp.opencv_objdetect.*;


import controller.Robotican;
import diagnosis.AnomalyDetector;

public class TestSet {
	Semaphore mutex = new Semaphore(1);
	private PrintWriter stdout;
	Sensor[] Sensors;
	private Robotican r;
	public TestSet(PrintWriter stdout,Robotican r) {
		this.stdout=stdout;
		this.r=r;
	}
	
	
	
	public void run(Sensor[] sensors, String testNum) throws java.lang.Exception {
		this.Sensors=sensors;
		final OpenKinectFrameGrabber grabber=new OpenKinectFrameGrabber(0);
		//grabber.setFormat("depth");
		int i=2;
		//grabber.start();
		//IplImage grabbedImage = grabber.grab();
		//CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());
		int direction =1,left=0,right=0,fwd=0,none=0,close=0;
		boolean stage1=false,stage2=false,stage3=false;
		//int [] 
		//mutex.acquire();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					grabber.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				IplImage grabbedImage = null;
				
				
				try {
					grabbedImage = grabber.grab();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());
				try {
					while (frame.isVisible() && (grabbedImage = grabber.grab()) != null) {
				//		 Pixel p=Kin.detectCheck(grabber);
				//		 if(p.x>0 && p.y> 0)
				//		 {
				//		  cvLine(grabbedImage, cvPoint( 30, 30 ),cvPoint( 30,60 ),CvScalar.RED, 4, CV_AA, 0);
				//		  cvLine(grabbedImage, cvPoint( 15 , 45),cvPoint( 45,45 ),CvScalar.RED, 4, CV_AA, 0);
				//		  cvLine(grabbedImage, cvPoint( p.x, p.y-15 ),cvPoint( p.x,p.y+15 ),CvScalar.RED, 4, CV_AA, 0);
				//		  cvLine(grabbedImage, cvPoint( p.x-15, p.y ),cvPoint( p.x+15,p.y ),CvScalar.RED, 4, CV_AA, 0);
				//		 }
	                       

						frame.showImage(grabbedImage);
					}
				}
				catch (java.lang.Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
		
		Test t=new Test(Sensors);
		
		
		final CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());
		while (frame.isVisible() && (grabber.grab()) != null) {
			grabber.start();
			final IplImage grabbedImage;	
			grabbedImage = grabber.grab();
			
			 final Pixel p=Kin.detectCheck(grabber);
			 

				if (p.x==-1)
				{
						//r.move(0,-0.2);
					    none++;
						System.out.println("no green");
					//System.out.println(i);
				
					//p= Kin.detectCheck(grabber);
				}	
				else if (p.x ==-2)
				{
					close++;
					System.out.println("too close");
					
					if(close ==3)
					{
						stage2=true;
						System.out.println("stage 2 has finished");
						
						break;
					}
				}
				else if(p.x < 200)
				{
					left++;
					System.out.println("from left");
				}
				else if (p.x > 400)
				{
					right++;
					System.out.println("from right");
				}
				else{ fwd++;
					//System.out.println(p.x+";"+p.y);
					System.out.println("center");
					}
				//Thread.sleep(1000);
				//i++;
				
				if(fwd==1 || left==2 || right==2 || none==3)
				//if(i==1)
				{
					if(fwd > Math.max(left, right)) 
						{ 
							r.move(0.2,0);
							direction=0;
						}
					else if(left>= right) direction=-1;
						else direction=1;	
					System.out.println(direction);
					if(direction!=0)
					{
						stage1=true;
						r.move(0, direction*0.3);
					}
					left=0;right=0;fwd=0;none=0;//i=0;
					
				}
			}
		if(stage2) //close enough to take object with IRs
		{
			
			if(left>= right) r.takeObjectFromLeft(); //direction=-1
			else r.takeObjectFromRight(); //direction=1
			//frame.showImage(grabbedImage);
			
		}
			
		//mutex.release();
		System.out.println("done stage 3");
	
		
	
		
		
		//THIS IS THE EXPERIMENT !!!
		
		PrintWriter exp=new PrintWriter(new FileWriter(testNum+".txt"));
		AnomalyDetector ad=new AnomalyDetector(16, exp);
		
		
		
		PrintWriter log=new PrintWriter(new FileWriter(testNum+".csv"));
		for(Sensor s: Sensors)
			log.print(s.name+",");
		log.println();
		long time=System.currentTimeMillis();
		int count=0;
		while ((System.currentTimeMillis()-time)<10000){
			double[] ans=t.sample(System.currentTimeMillis(), r);
			System.out.println("sample:");
			for(int i1=0;i1<Sensors.length;i1++){
				
				ad.addValue(Sensors[i1].name, ans[i1]);
				
				System.out.print(ans[i1]+",");
				log.print(ans[i1]+",");
			}
			System.out.println();
			
			ad.detectAnomalies();
//			double[] sonars=r.getSonars();
//			for(double d: sonars)
//				System.out.print(d+",");
//			System.out.println();
			log.println();
			exp.println(count/8);
			count++;
			Thread.sleep(125);
		}
		log.close();
		exp.close();   
	} 

	/*
	private void initSensors() {
		
		
		Sensors=new Sensor[15];
		Sensors[0]=new InfraRef(0,FaultType.Proper,0,"IR left");
		Sensors[1]=new InfraRef(1,FaultType.Proper,0,"IR right");
		Sensors[2]=new InfraRef(2,FaultType.Proper,0,"IR center");
		Sensors[3]=new UltraSonic(0,FaultType.Proper,0,"US left");
		Sensors[4]=new UltraSonic(1,FaultType.Proper,0,"US right");
		Sensors[5]=new UltraSonic(2,FaultType.Proper,0,"US center");
		Sensors[6]=new Wheels(0,FaultType.Proper,0,"Wheel_x");
		Sensors[7]=new Wheels(1,FaultType.Proper,0,"Wheel_z");
		Sensors[8]=new Servo(0,FaultType.Proper,0,"servo_1");
		Sensors[9]=new Servo(1,FaultType.Proper,0,"servo_2");
		Sensors[10]=new Servo(2,FaultType.Proper,0,"servo_3");
		Sensors[11]=new Servo(3,FaultType.Proper,0,"servo_4");
		Sensors[12]=new Servo(4,FaultType.Proper,0,"servo_5");
		Sensors[13]=new Servo(5,FaultType.Proper,0,"servo_6");
		Sensors[14]=new Servo(6,FaultType.Proper,0,"servo_7");
	}
*/
}




