package com.unwind.netTools;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

public class DiscoverRunner implements Runnable {
	private List<InetAddress> results;
	
	private String subnet;
	private Integer startAdd;
	private Integer numAdds;
	
	public DiscoverRunner(String subnet, Integer start, Integer steps) {
		this.subnet = subnet;
		this.startAdd = start;
		this.numAdds = steps;
		results = new LinkedList<InetAddress>();
	}
	
	@Override
	public void run() {
		int timeout=4000;
		   for (int i=startAdd;i<startAdd+numAdds;i++){
		       String host=subnet + "." + i;
		       try {
		    	   InetAddress a = InetAddress.getByName(host);
				if (a.isReachable(timeout)){
				    results.add(a);
					//System.out.println(host + " is reachable");
				   }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
	}
	
	public List<InetAddress> getResults(){
		return results;
	}

}
