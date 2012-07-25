package testCheater;

import controller.Robotican;

public class UltraSonic extends Sensor {



	public UltraSonic(int id, FaultType fault, int delay, String name) {
		super(id, fault, delay, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getSensor(Robotican robo) {
		return robo.getSonars()[ID];
	}

	@Override
	public void setSensor(double[] acts) {
		// TODO Auto-generated method stub
		
	}

}
