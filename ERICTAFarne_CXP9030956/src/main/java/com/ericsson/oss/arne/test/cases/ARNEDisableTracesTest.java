package com.ericsson.oss.arne.test.cases ;

import java.io.*;
import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.oss.arne.test.operators.GenericCLIOperator;
import com.ericsson.oss.arne.test.operators.GenericOperator;

import org.apache.log4j.Logger;

import org.testng.annotations.Test;

import javax.inject.Inject;


public class ARNEDisableTracesTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<GenericOperator> operatorRegistry;

    Logger logger = Logger.getLogger(ARNEDisableTracesTest.class);

    /**
     * @DESCRIPTION Import FtpServer and FtpServices
     * @PRE
     * @PRIORITY HIGH
     */

    @TestId(id = "ARNE_KGB1", title = "ARNE: Disbale Traces")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "ARNEDisableTracesTest")
    public void enableTracesForARNE(@Input ("scriptName") String scriptName,@Input ("remotePath") String remoteScriptPath) {

    	GenericCLIOperator importOperator = (GenericCLIOperator) operatorRegistry.provide(GenericOperator.class);

        
        logger.info("===== Transferring the script===== "+scriptName);
        if(scriptName.contains("disableTraces")){
	        try{
	        	importOperator.deployFiletoServer(scriptName, remoteScriptPath);
	        }
	        catch(FileNotFoundException fe){
	        	fe.printStackTrace();
	        }
	        
	        remoteScriptPath = remoteScriptPath+"/";
	        String command = remoteScriptPath + scriptName;
	   	 	long exitCode = importOperator.executeCommand(command);
	        System.out.println("Exit value = " + exitCode);
	        assertTrue(exitCode == 0);
        }

    }


}

