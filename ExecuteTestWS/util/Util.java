package util;

import java.util.concurrent.ArrayBlockingQueue;

public class Util {

	public static double stdev(ArrayBlockingQueue<Double> q){
		double x[]=new double[q.size()];
		int i=0;
		for(Double d : q){
			x[i]=d;
			i++;
		}
		return stdev(x);
	}
	
	public static double stdev(double[] x){
		double m=average(x);
		double sum=0;
		for(double d : x)
			sum+=(d-m)*(d-m);
		return Math.sqrt(sum/x.length);
	}

	public static double average(double[] x) {
		double sum=0;
		for(double d : x)
			sum+=d;
		
		return sum/x.length;
	}
	
	public static double pearson(double x[],double y[]){
		double xmean=0,ymean=0;
		for(int i=0;i<x.length;i++){
			xmean+=x[i];
			ymean+=y[i];
		}
		xmean/=x.length;
		ymean/=y.length;
		double xx=0,yy=0,xy=0;
		for(int i=0;i<x.length;i++){
			xx+=(x[i]-xmean)*(x[i]-xmean);
			yy+=(y[i]-ymean)*(y[i]-ymean);
			xy+=(x[i]-xmean)*(y[i]-ymean);
		}
		
		if(Double.isInfinite(xx) || Double.isInfinite(xy) || Double.isInfinite(yy))
			return 0;
		else{
			double r=Math.sqrt(xx*yy);
			
			
			if(r!=0)
				r=xy/r;
			
			if(r>1)
				r=1;
			
			if(r<-1)
				r=-1;

			return r;
			
		}
		
/*		if(Double.isInfinite(xx))
			xx=Double.MAX_VALUE;
		if(Double.isInfinite(xy))
			xy=Double.MAX_VALUE;
		if(Double.isInfinite(yy))
			yy=Double.MAX_VALUE;
		
		double r=Math.sqrt(xx*yy);
		if(r!=0)
			r=xy/r;
		//System.out.println(r+","+xx+","+yy+","+xy);
		return r;*/
	}

	public static double pearson(ArrayBlockingQueue<Double> a, ArrayBlockingQueue<Double> b) {
		double[] a1=new double[a.size()];
		double[] b1=new double[b.size()];
		int i=0;
		boolean all_eq1=true,all_eq2=true;
		for(Double d:a){
			a1[i]=d;
			if(i>0 && a1[i]!=a1[i-1])
				all_eq1=false;
			i++;
		}
			
		i=0;
		for(Double d:b){
			b1[i]=d;
			if(i>0 && b1[i]!=b1[i-1])
				all_eq2=false;
			i++;
		}
		
		if((all_eq1 || all_eq2) && a1.length>0){
			a1[0]=0;
			b1[0]=0;
		}
		return pearson(a1, b1);
	}
	
}
