package controller;

import java.io.IOException;
import java.io.PrintWriter;

public class Robotican implements ControllableRobot {
	
	private PrintWriter stdin;
	private int count=0;
	private volatile double[] servosPosition= new double[7];
	private volatile double[] wheelsPosition= new double[2];
	private volatile double[] irData= new double[3];
	private volatile double[] sonarData= new double[3];
	
	public Robotican(PrintWriter stdin) {
		this.stdin=stdin;
	}
	/*public double getArrays()
	{

		return	servosPosition[3];
		
	}*/
	public void setArrays(String line)
	{
		String[] tokens= line.split(" ");
		System.out.print("");
		stdin.flush();
		if(tokens[0].equalsIgnoreCase("servosPosition"))
		{
			for(int i=0; i< servosPosition.length ; i++)
			{
				servosPosition[i]= Double.parseDouble(tokens[i+1]);
				//System.out.println("afwfsa"+Double.parseDouble(tokens[i+1]));
			}
		}
		if(tokens[0].equalsIgnoreCase("wheelsPosition"))
		{
			for(int i=0; i< wheelsPosition.length ; i++)
			{
				wheelsPosition[i]= Double.parseDouble(tokens[i+1]);
			}
		}
		if(tokens[0].equalsIgnoreCase("irData"))
		{
			for(int i=0; i< irData.length ; i++)
			{
				irData[i]= Double.parseDouble(tokens[i+1]);
			}	
		}
		if(tokens[0].equalsIgnoreCase("sonarData"))
		{
			for(int i=0; i< sonarData.length ; i++)
			{
				sonarData[i]= Double.parseDouble(tokens[i+1]);
			}	
		}
		
		printSensors();
	}
	
	private void printSensors() {
		count++;
		System.out.println("==================================================================");
		for(double d: sonarData)
			System.out.print(d+",");
		for(double d: irData)
			System.out.print(d+",");
		for(double d: wheelsPosition)
			System.out.print(d+",");
		for(double d: servosPosition)
			System.out.print(d+",");
		System.out.println();
		System.out.println(count+"==================================================================");
	}
	@Override
	//moving for 1 second, at given speed and degree
	//move [forward m/s] [turn degree d/s] 
	public void move(double meterPerSec, double turnAngleDeg) {
		stdin.println("move "+meterPerSec+" "+turnAngleDeg);
		stdin.flush();
	}

	@Override
	public void turnInPlace(double deg) {
		stdin.println("move 0 "+deg);
		stdin.flush();
	}
	//get the position of the servos of the arm as an array - in the order: gripper, arm_2a,base1,arm_2b,arm_3,arm_1a,arm_1b
	//get_servos_position		
	@Override
	public double[] get_servos_position() {
		stdin.println("get_servos_position");		
		stdin.flush();

		return servosPosition;
	}
	//get the position (pose) of the robot using the odometry
	@Override
	public double[] get_wheels_position() {
		// TODO Auto-generated method stub
		
		stdin.println("get_wheels_position");
		stdin.flush();
		return wheelsPosition;
	}
	//moving for distance in meters, at given speed, degree, distance (can be float)
	//move_distance [forward m/s] [turn degree d/s] [distance m]
	@Override
	public void move_distance(double forward, double degree, double distance) {
		stdin.println("move_distance "+forward+" "+degree+" "+distance);
		stdin.flush();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move_time(double forward, double degree, double time) {
		// TODO Auto-generated method stub
		stdin.println("move_time "+forward+" "+degree+" "+time);
		stdin.flush();
		
	}

	@Override
	public void relaxArm() {
		// TODO Auto-generated method stub
		stdin.println("relax");
		stdin.flush();
	}
	public void takeObjectFromLeft() {
		// TODO Auto-generated method stub
		stdin.println("takel");
		stdin.flush();
	}
	public void takeObjectFromRight() {
		// TODO Auto-generated method stub
		stdin.println("taker");
		stdin.flush();
	}

	//moving arm to a given position
	//move_arm [azimuth deg] [elevation1 deg] [elevation2 deg] [roll deg]
	@Override
	public void moveArm(double AzimuthDeg, double elevationDeg1,
			double elevationDeg2, double rotationDeg) {
		stdin.println("move_arm "+AzimuthDeg+" "+elevationDeg1+" "+elevationDeg2+" "+rotationDeg);
		stdin.flush();
	}

	@Override
	public void grub(int power) {
		//stdin.println("rostopic pub arm_topic arbotix_msgs/arm_msg '{gripper_angle: 0.5}'");
		stdin.println("grab "+power);
		stdin.flush();
		// TODO Auto-generated method stub

	}

	@Override
	public void releaseGrub() {
		stdin.println("release");
		stdin.flush();
		// TODO Auto-generated method stub

	}

	@Override
	public void resetArm() {
		stdin.println("calibrate");
		stdin.flush();
		// TODO Auto-generated method stub

	}

	//get the Ultra-Sonic sensors data as an array - in the order: left, right, center
	@Override
	public double[] getSonars() {
		stdin.println("get_sonars");
		stdin.flush();
		// TODO Auto-generated method stub
		return sonarData;
	}

	@Override
	public double[] getInfras() {
		stdin.println("get_ir");
		stdin.flush();
		// TODO Auto-generated method stub
		return irData;
	}

	@Override
	public RGB[][] getCameraImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RGBD[][] getkinect() {
		// TODO Auto-generated method stub
		return null;
	}

}
