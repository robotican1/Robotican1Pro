package testCheater;
import java.util.concurrent.Callable;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenKinectFrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
public class detectPthread implements Callable<Pixel>
{
	public Pixel call()
	{
		final OpenKinectFrameGrabber grabber=new OpenKinectFrameGrabber(0);
		Pixel p=new Pixel(-1, -1);
	    try
	    {
	        // sleep for 10 seconds
	    	final CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());
			if (frame.isVisible() && (grabber.grab()) != null) {
				grabber.start();
				final IplImage grabbedImage;	
				grabbedImage = grabber.grab();
				
				p=Kin.detectCheck(grabber);
	         /*   if(p.x>0 && p.y> 0)
				 {
				  //cvLine(grabbedImage, cvPoint( 30, 30 ),cvPoint( 30,60 ),CvScalar.RED, 4, CV_AA, 0);
				  //cvLine(grabbedImage, cvPoint( 15 , 45),cvPoint( 45,45 ),CvScalar.RED, 4, CV_AA, 0);
				  cvLine(grabbedImage, cvPoint( p.x, p.y-15 ),cvPoint( p.x,p.y+15 ),CvScalar.BLUE, 4, CV_AA, 0);
				  cvLine(grabbedImage, cvPoint( p.x-15, p.y ),cvPoint( p.x+15,p.y ),CvScalar.BLUE, 4, CV_AA, 0);
				 }
				frame.showImage(grabbedImage);
           */
				//Pixel p=Kin.detectCheck(grabber);
			}
	    }
	    catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.lang.Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return (p);
	}
	}



