package com.ericsson.arne.data;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;

public class ARNEDataProvider implements DataProvider {

	//private static Host DEFAULTHOST = DataHandler.getHostByName("atvts554");
		//private static Host DEFAULTHOST = DataHandler.getHostByName("testServer");
	private static Host DEFAULTHOST = DataHandler.getHostByName("atvts694");
		
		public static Host getarneHost(){
			return DEFAULTHOST;
		}
		

		@Override
		public Class<? extends Annotation> annotationType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int[] indices() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String name() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean parallel() {
			// TODO Auto-generated method stub
			return false;
		}

}
