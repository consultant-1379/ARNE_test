package com.ericsson.oss.arne.test.cases ;

import java.io.*;
import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.oss.arne.test.operators.FtpImportCliOperator;
import com.ericsson.oss.arne.test.operators.FtpImportOperator;

import org.apache.log4j.Logger;

import org.testng.annotations.Test;

import javax.inject.Inject;


public class ARNE_KGBStartAdjust extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<FtpImportOperator> operatorRegistry;

    Logger logger = Logger.getLogger(ARNE_KGBStartAdjust.class);

    /**
     * @DESCRIPTION Import FtpServer and FtpServices
     * @PRE
     * @PRIORITY HIGH
     */

    @TestId(id = "ARNE_KGB2", title = "ARNE: StartAdjust XML Import")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "ARNE_KGBStartAdjust")
    public void startAdjustARNEImport(@Input ("path") String remotePath, @Input ("scriptName") String scriptName, @Input ("expected") long expected) {

    	FtpImportCliOperator importOperator = (FtpImportCliOperator) operatorRegistry.provide(FtpImportOperator.class);
    	
    	logger.info("===== Transferring the script===== "+scriptName);
        try{
        	importOperator.deployFiletoServer(scriptName, remotePath);
        }
        catch(FileNotFoundException fe){
        	fe.printStackTrace();
        }
       // int returnCode1 = importOperator.makeMAFOnline();
       // String command = "/opt/ericsson/fwSysConf/bin/startAdjust.sh";
		//logger.info("Executing ****  "+command+"  ****");		
        remotePath = remotePath+"/";
        String command = remotePath + scriptName;
        logger.info("Executing command "+command);
   	 	long exitCode = importOperator.executeCommand(command);
		//long returnCode = importOperator.executeCommand("/var/opt/ericsson/arne/ARNE_KGB/test.sh");
		logger.info("Execution completed ");
		assertTrue(exitCode == expected);
    }


}

