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


public class FtpServerImportTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<FtpImportOperator> operatorRegistry;

    Logger logger = Logger.getLogger(FtpServerImportTest.class);

    /**
     * @DESCRIPTION Import FtpServer and FtpServices
     * @PRE
     * @PRIORITY HIGH
     */

    @TestId(id = "ARNE_KGB1", title = "ARNE: FTPServer Import")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "FtpServerImportTest")
    public void ftpServicesARNEImport(@Input ("scriptName") String scriptName,@Input ("path") String remoteScriptPath,@Input ("output") long expected) {

    	FtpImportCliOperator importOperator = (FtpImportCliOperator) operatorRegistry.provide(FtpImportOperator.class);

        
        logger.info("===== Transferring the script===== "+scriptName);
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
        assertTrue(exitCode == expected);
        //int returnCode = importOperator.makeMAFOffline();
        //assertTrue(returnCode == expected);
    }


}

