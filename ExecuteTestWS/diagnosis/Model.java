package diagnosis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public class Model {

	private class Component{	// a component
		String name;
		String dependesOn;
		public Component(String name,String dependesOn) {
			this.name=name;
			this.dependesOn=dependesOn;
		}
	}

	Component[] components;
	
	ConcurrentHashMap<String,String> suspected;
	ConcurrentHashMap<String,String> suspectedHistory;
	ConcurrentHashMap<String,String> dependencies;	// components dependencies
	
	int reports=0;
	Object lock=new Object();

	private PrintWriter out;
	
	public Model(PrintWriter out) {
//		components=new Component[14];
//		components[0]=new Component("airspeed-indicator", "pitot");
//		components[1]=new Component("altimeter","static system");
//		components[2]=new Component("attitude-indicator","vacuum");
//		components[3]=new Component("dme","electrical system");
//		components[4]=new Component("encoder","static system");
//		components[5]=new Component("gps","electrical system");
//		components[6]=new Component("heading-indicator","vacuum");
//		components[7]=new Component("heading-indicator-fg","vacuum");
//		components[8]=new Component("magnetic-compass","independed");
//		components[9]=new Component("slip-skid-ball","vacuum");
//		components[10]=new Component("turn-indicator","vacuum");
//		components[11]=new Component("vertical-speed-indicator","static system");
//		components[12]=new Component("airspeed-indicator", "static system");
//		components[13]=new Component("engine", "engine system");
//		
//		dependencies=new ConcurrentHashMap<String, String>();
//		for(int i=0;i<components.length;i++){
//			String v=dependencies.get(components[i].dependesOn);
//			if(v==null)
//				v=components[i].name;
//			else
//				v+=","+components[i].name;
//			
//			dependencies.put(components[i].dependesOn, v);
//		}
		
		//loadModel("model2.txt",108);
		loadModel("robotican_model.txt",15);
		this.out=out;
		suspected=new ConcurrentHashMap<String, String>();
		suspectedHistory=new ConcurrentHashMap<String, String>();
	}
	
	
	
	
	private void loadModel(String fileName,int size) {
		try {
			BufferedReader in=new BufferedReader(new FileReader(fileName));
			String line,internalComponent="";			
			components=new Component[size];
			dependencies=new ConcurrentHashMap<String, String>();
			int i=0;
			while((line=in.readLine())!=null){
				//System.out.println(line+" "+i);
				if(line.startsWith("*"))
					internalComponent=line.substring(1);
				else{
					components[i]=new Component(line, internalComponent);
					String v=dependencies.get(components[i].dependesOn);
					if(v==null)
						v=components[i].name;
					else
						v+=","+components[i].name;
					
					dependencies.put(components[i].dependesOn, v);
					i++;
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	public String getDependency(String name){

		for(int i=0;i<components.length;i++)
			if(name.contains(components[i].name))
				return components[i].dependesOn;
		
		return null;
	}

	public void reportSuspected(final String sensor, String status) {
		
		
		suspected.put(sensor, status);
		suspectedHistory.put(sensor, status);
		
//		if(sensor.contains("heading") || sensor.contains("turn") || sensor.contains("skid"))
//			System.out.println("\t\t"+sensor+" --> "+status);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (lock) {
					suspectedHistory.remove(sensor);	
				}				
			}
		}).start();
	}

	public void reportCorrelated(String sensor, String correlated, String correlatedStatus) {
		String v=suspected.get(sensor);
		v+=","+correlated+"@"+correlatedStatus;
		suspected.put(sensor, v);
	}

//	public void generateReport(){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(5*1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				generateReport1();				
//			}
//		}).start();
//		
//	}
	
	
	public boolean generateReport() {
		boolean ret=false;
//		if(suspected.size()>0){
//			reports++;
//			out.println("============ report "+reports+" ===========");
//		}
		
		for(String sensor : suspected.keySet()){
			String sp[]=suspected.get(sensor).split(",");
			String sensorStatus=sp[0];
			
			boolean suspectedSingleFailure=false;
			boolean internalComponentFailure=false;
			
			String correlated=null;
			String correlatedStatus=null;
			String sd=null;
			String cd=null;
			
			boolean sensor_from_different_system_with_different_status_exist=false;
			boolean sensor_from_different_system_with_same_status_exist=false;
			
//			if(sensor.contains("heading") || sensor.contains("turn") || sensor.contains("skid")){
//				System.out.println("\t\t"+sensor+" --> "+(sp.length-1));
//				System.out.println("\t\t"+suspected.get(sensor));
//			}
			
			
			for(int i=1;i<sp.length;i++){
				correlated=sp[i].split("@")[0];
				correlatedStatus=sp[i].split("@")[1];
				sd=getDependency(sensor);
				cd=getDependency(correlated);
				
				// if a correlated sensor of the another dependency didn't react in the same manner then the sensor is faulty 
				if(sd!=null && cd!=null && !correlatedStatus.equals(sensorStatus) && !sd.equals(cd)){
					//suspectedSingleFailure=true;
					//break;
					
					sensor_from_different_system_with_different_status_exist=true;
				}
				if(sd!=null && cd!=null && correlatedStatus.equals(sensorStatus) && !sd.equals(cd)){
					sensor_from_different_system_with_same_status_exist=true;
					break;
				}
			}
			
			if(sensor_from_different_system_with_different_status_exist && !sensor_from_different_system_with_same_status_exist)
				suspectedSingleFailure=true;
			
			// we need to check weather or not an internal component caused it
			if(suspectedSingleFailure){
				ret=true;
				
				out.println(sensor+ " is suspected of the fault of being ["+sensorStatus+"]");
				
				String dependedSensors[]= dependencies.get(sd).split(",");
				internalComponentFailure=true;
				float f=0;
				for(int j=0;j<dependedSensors.length;j++){
					out.print("\t\t"+dependedSensors[j]+" - ");
					// if there is a sensor of the same dependency that wasn't suspected 
					if(!isSuspected(dependedSensors[j])){// then the internal component is not suspected
						internalComponentFailure=false;
						out.println("X");
					} else{
						out.println("V");
						f++;
					}
				}
				
				if(internalComponentFailure)
					out.println("\t"+sd+ " is also suspected of being faulty\n");
				else
					out.println("\t"+sd+" is "+(100*f/dependedSensors.length)+"% suspected");

			}
		}
			
		
		suspected.clear();
		return ret;
	}




	private boolean isSuspected(String s) {
		if(suspected.get(s)!=null){
			out.print(suspected.get(s).split(",")[0]+" - ");
			return true;
		}
		if(suspectedHistory.get(s)!=null){
			out.print(suspectedHistory.get(s).split(",")[0]+" in recent history - ");
			return true;
		}
		return false;
//		for(String sensor: suspected.keySet())
//			if(sensor.contains(s)){
//				System.out.print(suspected.get(sensor).split(",")[0]+" - ");
//				return true;
//			}
//		boolean b=false;
//		synchronized (lock) {
//			for(String sensor: suspectedHistory.keySet())
//				if(sensor.contains(s)){
//					System.out.print(suspectedHistory.get(sensor).split(",")[0]+" in recent history - ");
//					b= true;
//				}
//		}
//		return b;
	}
}

