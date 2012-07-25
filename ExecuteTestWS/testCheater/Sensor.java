package testCheater;

import controller.Robotican;

public abstract class Sensor{
	
	
	int ID;
	FaultType Fault;
	int Delay;
	public String name;
	
	public Sensor(int id,FaultType fault,int delay,String name){
		this.ID=id;
		this.Fault=fault;
		this.Delay=delay;
		this.name=name;
		
	}
	
	public abstract double getSensor(Robotican robo);
	public abstract void setSensor(double[] acts);
	
}
