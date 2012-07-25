package testCheater;

import controller.Robotican;

public class Wheels extends Sensor{


	public Wheels(int id, FaultType fault, int delay, String name) {
		super(id, fault, delay, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getSensor(Robotican robo) {
		// TODO Auto-generated method stub
		return robo.get_wheels_position()[ID];
	}

	@Override
	public void setSensor(double[] acts) {
		// TODO Auto-generated method stub
		
	}

}
