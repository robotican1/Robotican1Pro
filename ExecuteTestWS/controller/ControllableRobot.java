package controller;

public interface ControllableRobot {

	public void move(double meterPerSec, double turnAngleDeg);
	public void move_time(double forward, double degree,double time);
    public void move_distance(double forward,double degree,double distance);
	public void turnInPlace(double deg);
	public void moveArm(double AzimuthDeg,double elevationDeg1,double elevationDeg2,double rotationDeg);
	public void grub(int power);
	public void releaseGrub();
	public void resetArm(); //calibrate
	public void relaxArm();
	public double[] getSonars();
	public double[] getInfras();
	public double[] get_wheels_position();
	public double[] get_servos_position();		
	public RGB[][] getCameraImage();
	public RGBD[][] getkinect();
}
