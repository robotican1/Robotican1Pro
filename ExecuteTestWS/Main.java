import testCheater.FaultType;
import testCheater.InfraRef;
import testCheater.Sensor;
import testCheater.Servo;
import testCheater.UltraSonic;
import testCheater.Wheels;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class Main {

	public static void runTest(String name,String IRL, String IRR, String IRC,String USL,String USR, String USC, String servo1, String servo2, String servo3, String servo4, String servo5, String servo6, String servo7, String wheelsX, String wheelsZ )
	{
		//System.out.println(IRL);
		//WriteNameToFile(name);
		try {
			FileWriter fstream = new FileWriter("E:\\Gali\\My Documents\\workspace\\ExecuteTestWSClient\\WebContent\\sampleMainProxy\\Res\\Tests.txt",true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.append(name);out.newLine();
			out.close();	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Sensor[] Sensors=new Sensor[15];
		String fault= IRL.split(";")[0];
		FaultType faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[0]=new InfraRef(0,faultT,Integer.parseInt(IRL.split(";")[1]),"IR left");
		
		fault= IRR.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[1]=new InfraRef(1,faultT,Integer.parseInt(IRR.split(";")[1]),"IR right");
		
		fault= IRC.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[2]=new InfraRef(2,faultT,Integer.parseInt(IRC.split(";")[1]),"IR center");
		
		fault= USL.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[3]=new UltraSonic(0,faultT,Integer.parseInt(USL.split(";")[1]),"US left");
		
		fault= USR.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[4]=new UltraSonic(1,faultT,Integer.parseInt(USR.split(";")[1]),"US right");
		
		fault= USC.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[5]=new UltraSonic(2,faultT,Integer.parseInt(USC.split(";")[1]),"US center");
		
		fault= wheelsX.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[6]=new Wheels(0,faultT,Integer.parseInt(wheelsX.split(";")[1]),"Wheel_x");
		
		fault= wheelsZ.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[7]=new Wheels(1,faultT,Integer.parseInt(wheelsZ.split(";")[1]),"Wheel_z");
		
		fault= servo1.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[8]=new Servo(0,faultT,Integer.parseInt(servo1.split(";")[1]),"servo_1");
		
		fault= servo2.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[9]=new Servo(1,faultT,Integer.parseInt(servo2.split(";")[1]),"servo_2");
		
		fault= servo3.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[10]=new Servo(2,faultT,Integer.parseInt(servo3.split(";")[1]),"servo_3");
		
		fault= servo4.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[11]=new Servo(3,faultT,Integer.parseInt(servo4.split(";")[1]),"servo_4");
		
		fault= servo5.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[12]=new Servo(4,faultT,Integer.parseInt(servo5.split(";")[1]),"servo_5");
		
		fault= servo6.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[13]=new Servo(5,faultT,Integer.parseInt(servo6.split(";")[1]),"servo_6");
		
		fault= servo7.split(";")[0];
		faultT=null;
		if(fault=="Proper"){faultT=FaultType.Proper;}if(fault=="Drift-Down"){faultT=FaultType.Drift;}if(fault=="Drift-Up"){faultT=FaultType.DriftUp;}
		if(fault=="Stuck"){faultT=FaultType.Stuck;}
		Sensors[14]=new Servo(6,faultT,Integer.parseInt(servo7.split(";")[1]),"servo_7");
		try {
			for(int i=0; i<Sensors.length;i++){
				System.out.println(Sensors[i].name);
			}
			//boot.Boot.main(Sensors, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*public String[] getTestsName()
	{
		List<String> names = new ArrayList<String>();
		 FileInputStream fstream=null;
		try {
			fstream = new FileInputStream("Tests.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  // Get the object of DataInputStream
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  try {
			while ((strLine = br.readLine()) != null)   
			  {
				  // Print the content on the console
				names.add(strLine);
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			  //Close the input stream
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String[])names.toArray();
	}*/

	private void WriteNameToFile(String name){
		  try{
			  // Create file 
			  FileWriter fstream = new FileWriter("Tests.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(name);
			  out.newLine();
			  //Close the output stream
			  out.close();
			  }
		  catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
	}
}
