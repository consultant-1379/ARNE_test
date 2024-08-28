package com.ericsson.oss.arne.test.cases;



import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

//import java.util.*;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.arne.data.ARNEDataProvider;
import com.ericsson.arne.operators.ARNEOperator;

public class ARNETestCase1 extends TorTestCaseHelper implements TestCase{
	ARNEOperator arneOperator = new ARNEOperator();
	
	
	@VUsers(vusers = {5})
	@Context(context = {Context.API})
	@Test (groups = {"ARNEtestCase1"})
	public void pMS_Commands_Creation(){
		setTestcase("ARNE_CommandsCreation","ARNE_Tests");
		setTestStep("Execute the ARNE test suite based on the test server setup");
		setTestStep("Get these commands back to TAF workspace to execute these commands");
		boolean expected,actual;
		actual=arneOperator.operate(ARNEDataProvider.getarneHost());
		expected=arneOperator.expected();
		System.out.println("\n******actual result :"+actual+"\n");
		System.out.println("\n******Expected result :"+expected+"\n");
		AssertJUnit.assertEquals(actual,expected);
	}
}