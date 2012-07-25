package boot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import testCheater.Sensor;
import testCheater.TestSet;
import controller.Robotican;
		
public class Boot {


	private static void UpdateServosPosition(Robotican r,String line)
	{
//		System.out.println("boo");
//		System.out.println("line="+line);
		r.setArrays(line);
		
	}
	/**
	 * @param args
	 */
	

	public static void main(Sensor[] sen,String testNum) throws Exception{		
		

   		File wd = new File("/bin");
		System.out.println(wd);
		Process robotControl=Runtime.getRuntime().exec("/bin/bash",null,wd);
		//Process robotControl=Runtime.getRuntime().exec("python ~/ros_workspace/arbotix/arbotix_python/nodes/example.py",null,wd);
		PrintWriter rc_stdin= new PrintWriter(robotControl.getOutputStream());
		final Robotican r=new Robotican(rc_stdin);
		final BufferedReader cstdout = new BufferedReader(new InputStreamReader(robotControl.getInputStream()));
		new Thread(new Runnable() {
			@Override
			public void run() {
				String line;
				try {
					while ((line = cstdout.readLine())!=null) {
							if(line.contains("servosPosition")||line.contains("irData")||
									line.contains("wheelsPosition")||line.contains("sonarData"))
										UpdateServosPosition(r, line);
					        System.out.println(line);
					}
					cstdout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		final BufferedReader cstderr = new BufferedReader(new InputStreamReader(robotControl.getErrorStream()));
		if(cstderr==null)
			System.out.println("WTF!");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String line;
				try {
					while ((line = cstderr.readLine())!=null) {
					        System.out.println("**"+line);
					}
					cstderr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
		rc_stdin.println("cd ~/ros_workspace");
		rc_stdin.println(". setup.sh");

		rc_stdin.println("python ~/ros_workspace/arbotix/arbotix_python/nodes/master.py");
		
		
		TestSet ts=new TestSet(rc_stdin,r);
		Sensor[] sensors = null;
		ts.run(sensors,"");
		rc_stdin.println("exit");
		rc_stdin.flush();
		
		
		Thread.sleep(1*30000);
		rc_stdin.close();
		cstdout.close();
		cstderr.close();
		
		//System.out.println("output="+r.get_servos_position()[1]);
		robotControl.destroy();
		
       	
		
		System.out.println("done main");
		
		
		
		

	}

}
