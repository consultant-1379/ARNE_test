package com.ericsson.arne.operators;

import com.ericsson.arne.operators.api.ARNEApiOperator;
import com.ericsson.arne.operators.api.ARNEApiOperator1;
import com.ericsson.cifwk.taf.GenericOperator;
import com.ericsson.cifwk.taf.data.Host;

public class ARNEOperator implements GenericOperator {

	ARNEApiOperator ARNEApi = new ARNEApiOperator();
	ARNEApiOperator1 ARNEApi1 = new ARNEApiOperator1();
	
	 public boolean operate(Host host){
	 	return ARNEApi.operate(host);
	  	
	    }
	 
	 public boolean operate1(Host host){
		 return ARNEApi1.operate(host);
		  	
		    }
	 

	    public boolean expected() {
	    	return true;
	    }

}
