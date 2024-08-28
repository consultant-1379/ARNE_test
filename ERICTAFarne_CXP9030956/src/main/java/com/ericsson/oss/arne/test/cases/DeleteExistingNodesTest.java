/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.arne.test.cases;

import java.io.*;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.arne.test.operators.GenericOperator;
import com.ericsson.oss.arne.test.operators.GenericCLIOperator;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
public class DeleteExistingNodesTest extends TorTestCaseHelper implements TestCase{
	
	@Inject
    OperatorRegistry<GenericOperator> operatorRegistry;
	 Logger logger = Logger.getLogger(DeleteExistingNodesTest.class);
	long value = 0;
	
	 @TestId(id = "ARNE_KGB", title = "ARNE: Export and Delete the Existing nodes")
     @Test(groups = {"KGB"})
     @Context(context = {Context.CLI})
     @DataDriven(name = "DeleteExistingNodesTest")
     public void deleteNodesMethod(@Input("scriptName") String scriptName,
                     @Input("remoteScriptPath") String remotePath,
                     @Output("output") long expected) {
		 
		 GenericCLIOperator operator = (GenericCLIOperator) operatorRegistry.provide(GenericOperator.class);
		 
         try
         {
             System.out.println("sending\n");
             operator.deployFiletoServer(scriptName, remotePath);
             //operator.sendFileRemotely("test_connection.tcl", remotePath);
         }
         catch (FileNotFoundException e)
         {
             e.printStackTrace();
         }
         
         if(scriptName.startsWith("Netsim")){
        	 String command = remotePath + "/" + scriptName;
        	 logger.info("===== Making MAF offline =====");
        	 int returnCode = operator.makeMAFOffline();
        	 if(returnCode == 0){
        		 logger.info("===== Executing Delete usecase =====");
        		 value = operator.executeCommand(command);
        		 if(value == expected){
        			 logger.info("===== Making MAF online =====");
        			 value = operator.makeMAFOnline();
        				if(value == 0){
        					logger.info("===== Executing StartAdjust script =====");
        					value = operator.executeCommand("/opt/ericsson/fwSysConf/bin/startAdjust.sh");
        				}
        				else{
        					logger.info("===== MAF online failure =====");
        					value = 1;
        				}
        		 }
        		 else if(value == 2){
        			 logger.info("===== No Need to run delete usecase as there are no Nodes =====");
        			 value = 0;
        		 }
        		 else{
        			 logger.info("===== Delete Usecase failure =====");
        			 value = 1;
        		 }
        	 }
        	 else{
        		 logger.info("===== MAF offline failure =====");
        		 value = 1;
        	 }
             System.out.println("Actual value = " + value);
             assertTrue(value == expected);
         }
     }

}
