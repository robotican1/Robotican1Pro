package testCheater;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenKinectFrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


/*
class grabberThread
implements Callable<Pixel>
{
public Pixel call()
{
	final OpenKinectFrameGrabber grabber=new OpenKinectFrameGrabber(0);
	Pixel p=new Pixel(-1, -1);
    try
    {
		   ExecutorService service = null;
	        Future<Pixel>  task;

	       
        // sleep for 10 seconds
    	final CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());
		while (frame.isVisible() && (grabber.grab()) != null) {
			grabber.start();
			final IplImage grabbedImage;	
			grabbedImage = grabber.grab();
			
			


		        try 
		        {
		         
		        service = Executors.newFixedThreadPool(1);        
		        task    = service.submit(new detectPthread());
		            // waits the 10 seconds for the Callable.call to finish.
		            p = task.get();

		           // System.out.println(str);
		           // frame.showImage(grabbedImage);
		        }
		        catch(final InterruptedException ex)
		        {
		            ex.printStackTrace();
		        }
		        catch(final ExecutionException ex)
		        {
		            ex.printStackTrace();
		        }
		
		        service.shutdownNow();
			
			
			
			
			//p=Kin.detectCheck(grabber);
            if(p.x>0 && p.y> 0)
			 {
			  //cvLine(grabbedImage, cvPoint( 30, 30 ),cvPoint( 30,60 ),CvScalar.RED, 4, CV_AA, 0);
			  //cvLine(grabbedImage, cvPoint( 15 , 45),cvPoint( 45,45 ),CvScalar.RED, 4, CV_AA, 0);
			  cvLine(grabbedImage, cvPoint( p.x, p.y-15 ),cvPoint( p.x,p.y+15 ),CvScalar.BLUE, 4, CV_AA, 0);
			  cvLine(grabbedImage, cvPoint( p.x-15, p.y ),cvPoint( p.x+15,p.y ),CvScalar.BLUE, 4, CV_AA, 0);
			 }
			frame.showImage(grabbedImage);

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
*/




public class grabberThread implements Runnable{
	final OpenKinectFrameGrabber grabber=new OpenKinectFrameGrabber(0);
		   public grabberThread() throws java.lang.Exception {
		       // store parameter for later user
			   final CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / grabber.getGamma());
			   Pixel p=new Pixel(-1, -1);
			   ExecutorService service = null;
		        Future<Pixel>  task;

				while (frame.isVisible() && (grabber.grab()) != null) {
					grabber.start();
					final IplImage grabbedImage;	
					grabbedImage = grabber.grab();
					 service = Executors.newFixedThreadPool(1);        
				        task    = service.submit(new detectPthread());
				            // waits the 10 seconds for the Callable.call to finish.
				            p = task.get();
					//Pixel p=Kin.detectCheck(grabber);
					if(p.x>0 && p.y> 0)
					 {
					  //cvLine(grabbedImage, cvPoint( 30, 30 ),cvPoint( 30,60 ),CvScalar.RED, 4, CV_AA, 0);
					  //cvLine(grabbedImage, cvPoint( 15 , 45),cvPoint( 45,45 ),CvScalar.RED, 4, CV_AA, 0);
					  cvLine(grabbedImage, cvPoint( p.x, p.y-15 ),cvPoint( p.x,p.y+15 ),CvScalar.BLUE, 4, CV_AA, 0);
					  cvLine(grabbedImage, cvPoint( p.x-15, p.y ),cvPoint( p.x+15,p.y ),CvScalar.BLUE, 4, CV_AA, 0);
					 }
					
					frame.showImage(grabbedImage);
				}
		   }
			   
		   

		   public void run() {
		   }
		}



