package diagnosis;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Util;

public class AnomalyDetector extends JFrame{
	
	private class MyPanel extends JPanel{
		public void paint(Graphics g){
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.black);
			
			if(correlated!=null){
				int i=1;
				for(String s : correlated){
					g.drawString(s, 10, i*13);
					i++;
				}
			}
		}
	}
	
	private class Var{
		
		ArrayBlockingQueue<Double> vals;
		ArrayBlockingQueue<Double> lastWinVals;
		double lastVal=0;
		String name;
		
		public Var(String name) {
			this.name=name;
			vals=new ArrayBlockingQueue<Double>(windowSize);
			lastWinVals=new ArrayBlockingQueue<Double>(windowSize);
		}

		public void addValue(double var) {
			double p;
			if(vals.size()==windowSize){
				p=vals.poll();
				if(lastWinVals.size()==windowSize)
					lastWinVals.poll();
				lastWinVals.add(p);
			}
			vals.add(var);

			lastVal=var;
		}
	}

	private int windowSize;
	HashMap<String,Var> vars;
	double threshold=0.8;
	ArrayBlockingQueue<String> correlated=new ArrayBlockingQueue<String>(200);
	MyPanel p;
	Model cessna;
	
	public AnomalyDetector(int windowSize,PrintWriter out) {
		
		cessna=new Model(out);
		
		this.windowSize=windowSize;
		vars=new HashMap<String, AnomalyDetector.Var>();
		p=new MyPanel();
		p.setSize(500, 500);
		setSize(501, 501);
		add(p);
		//pack();
		setVisible(true);		
	}
	
	public void addValue(String name,double var){
		Var v=vars.get(name);
		if(v==null){
			v=new Var(name);
			vars.put(name, v);
		}
		v.addValue(var);		
	}
	
	
	double[][] rrm,rdm,drm,ddm;
	
	public void checkCorrelation(){
	//	correlated=new ArrayBlockingQueue<String>(100);
		
		if(rrm==null){
			rrm=new double[vars.size()][vars.size()];
//			drm=new double[vars.size()][vars.size()];
//			rdm=new double[vars.size()][vars.size()];
//			ddm=new double[vars.size()][vars.size()];
		}
		
		int i=0;
		for(Var v : vars.values()){
			int j=0;
			String cor=v.name;
			for(Var v1 : vars.values()){
				if(v1!=v && v.lastWinVals.size()==v1.lastWinVals.size() /*&& v.vals.size()==v1.diffs.size()*/){
					
					double rr=Util.pearson(v.lastWinVals, v1.lastWinVals);
//					double rr=Math.abs(Util.pearson(v.vals, v1.vals));
//					double rd=Math.abs(Util.pearson(v.vals, v1.diffs));
//					double dr=Math.abs(Util.pearson(v.diffs, v1.vals));
//					double dd=Math.abs(Util.pearson(v.diffs, v1.diffs));
					
					if(rr>threshold)
						cor+=" , "+v1.name;
//					if(rr>threshold)
//						correlated.add(v.name+" <--> "+v1.name+" R-R");						
//					if(rd>threshold)
//						correlated.add(v.name+" <--> "+v1.name+" R-D");						
//					if(dr>threshold)
//						correlated.add(v.name+" <--> "+v1.name+" D-R");						
//					if(dd>threshold)
//						correlated.add(v.name+" <--> "+v1.name+" D-D");

//					if(rr<0.2 && rrm[i][j]>threshold)
//						correlated.add(v.name+" <--> "+v1.name+" R-R");
//					if(rd<0.2 && rdm[i][j]>threshold)
//						correlated.add(v.name+" <--> "+v1.name+" R-D");
//					if(dr<0.2 && drm[i][j]>threshold)
//						correlated.add(v.name+" <--> "+v1.name+" D-R");
//					if(dd<0.2 && ddm[i][j]>threshold)
//						correlated.add(v.name+" <--> "+v1.name+" D-D");
					
					rrm[i][j]=rr;
//					rdm[i][j]=rd;
//					drm[i][j]=dr;
//					ddm[i][j]=dd;
				
				}
				j++;
			}
			//correlated.add(cor);
			i++;
		}
		//setTitle(correlated.size()+"");
//		p.repaint();
	}
	
	String[] status=new String[200];
	int count=0;
	public boolean detectAnomalies(){
		checkCorrelation();
		int[] suspected=new int[200]; // suspected indices
		String[] sensors=new String[200];
		boolean[] statusChanged=new boolean[200];
		int k=0;
		
		correlated=new ArrayBlockingQueue(200);
		int i=0;
		for(Var v : vars.values()){
			sensors[i]=new String(v.name);
			String s;
			if(status[i]!=null)
				s=new String(status[i]);
			else s="@";
			
			if(checkStuck(v.vals))
				status[i]="stuck";
			else
				if(checkDrop(v.vals))
					status[i]="drift down";
				else
					if(checkDrift(v.vals))
						status[i]="drift up";
					else
					status[i]="ok";

//			if(v.name.contains("US center"))
				correlated.add(v.name+" "+s+" --> "+status[i]);
			
			if(!s.equals("@") && !s.equals(status[i]) && !status[i].equals("ok")){
//				correlated.add(v.name+" "+s+" --> "+status[i]);
//				System.out.println(v.name+" "+s+" --> "+status[i]);
				suspected[k]=i;
				statusChanged[i]=true;
				k++;
				
			}else
				statusChanged[i]=false;
			
//			if(v.name.contains("attitude"))
//				correlated.add(v.name+" "+status[i]+" "+v.lastVal);
			i++;
		}
		
		// check each suspect with its correlated sensor
		for(i=0;i<k;i++){
			cessna.reportSuspected(sensors[suspected[i]],status[suspected[i]]);
			
			for(int j=0;j<rrm[suspected[i]].length;j++)
				if (rrm[suspected[i]][j]>threshold && count>2*windowSize){	// its a correlated sensor
					
					cessna.reportCorrelated(sensors[suspected[i]],sensors[j],status[j]);
					
//					if(!status[suspected[i]].equals(status[j]) && !statusChanged[j]){ // then ! probably both sensors were changed due to the same action (or system failure)
//						//System.out.println(sensors[suspected[i]]+" - "+status[suspected[i]]+" opposed to "+sensors[j]+" - "+status[j]);						
//					}
//					if(status[suspected[i]].equals(status[j])){ // same action or same failure on the system they both depend on
//						if(cessna.getDependency(sensors[suspected[i]])!=null && cessna.getDependency(sensors[suspected[i]])==cessna.getDependency(sensors[j])){
//							System.out.println("might be a problem in "+cessna.getDependency(sensors[j])+" because "+sensors[suspected[i]]+" changed its staus to "+status[suspected[i]]+" and "+sensors[j]+" is also "+status[j]);
//						}// else its probably due to the same action effect
//					}
				
				}
		}
		
		boolean ret=cessna.generateReport();
		
		p.repaint();
		count++;
		return ret;
	}

	
	
	private boolean checkStuck(ArrayBlockingQueue<Double> vals) {
		double lastval=vals.peek();
		for(Double d : vals){
			if(d!=lastval)
				return false;
			lastval=d;
		}
		return true;
	}

	private boolean checkDrop(ArrayBlockingQueue<Double> vals) {
		double lastval=Double.MAX_VALUE;
		for(Double d : vals){
			if(d>=lastval)
				return false;
			lastval=d;
		}
		return true;
	}
	
	private boolean checkDrift(ArrayBlockingQueue<Double> vals) {
		double lastval=Double.MIN_VALUE;
		for(Double d : vals){
			if(d<=lastval)
				return false;
			lastval=d;
		}
		return true;
	}
	
//	// for test
//	public static void main(String[] args){
//		ArrayBlockingQueue<Double> a=new ArrayBlockingQueue<Double>(4);
//		a.add(1.0);
//		a.add(1.0);
//		a.add(1.0);
//		a.add(1.0);
//		AnomalyDetector ad=new AnomalyDetector(100);
//		System.out.println(ad.checkDrop(a));
//	}
}
