package testCheater;
import java.util.Date;

import controller.Robotican;

public class Test {
	
	Sensor[] Sensors;
	int interval=0;
	long time;
	boolean started=false;
	double[] sampledSensors;
	
	public Test(Sensor[] sensors)
	{
		this.Sensors=sensors;
		sampledSensors=new double[Sensors.length];
	}

	public double[] sample(long sTime,Robotican robo)
	{
		if(!started){
			started=true;
			this.time=sTime;
		}
		for(int i=0;i<Sensors.length;i++){
			if(interval==0){
				sampledSensors[i]=Sensors[i].getSensor(robo);
			}
			else{
				if(Sensors[i].Fault==FaultType.Proper)
					sampledSensors[i]=Sensors[i].getSensor(robo);
				else{		
					if(sTime-time>=Sensors[i].Delay){	
						
						if(Sensors[i].Fault==FaultType.Drift){
							sampledSensors[i]=sampledSensors[i]-(1.0/100.0);
					/*		if(sampledSensors[i]<=0)
								sampledSensors[i]=0;
							else{
								//System.out.println(sampledSensors[i]);
								sampledSensors[i]=sampledSensors[i]-(1.0/100.0);
								//System.out.println(">>"+sampledSensors[i]);
							}*/
						}
						if(Sensors[i].Fault==FaultType.DriftUp){
							sampledSensors[i]=sampledSensors[i]+(1.0/100.0);
						}
					}
					else {
						sampledSensors[i]=Sensors[i].getSensor(robo);
					}
				}
			}
		}
		interval++;
		return sampledSensors;
	}

}
