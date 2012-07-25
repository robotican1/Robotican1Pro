package testCheater;

import controller.Robotican;

public class InfraRef extends Sensor {


	public InfraRef(int id, FaultType fault, int delay, String name) {
		super(id, fault, delay, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public  double getSensor(Robotican rob)
	{
		return rob.getInfras()[ID];
		
	}

	@Override
	public void setSensor(double[] acts) {
		// TODO Auto-generated method stub
		
	}

	

}
