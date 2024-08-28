package com.ericsson.oss.arne.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.oss.arne.test.operators.DeleteCliOperator;
import com.ericsson.oss.arne.test.operators.DeleteOperator;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class Delete_PerformanceTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<DeleteOperator> deleteProvider;

    Logger logger = Logger.getLogger(Delete_PerformanceTest.class);
    /**
     * @DESCRIPTION Delete 1 RNC, 2 RXI , 400 RBS
     * @PRE 
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-33022_Perf_1", title = "OSS-33022_Perf_1: ARNE Import_delete")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB","VCDB","STCDB"})
    @DataDriven(name = "delete_performancetest")
    public void oSS33022_Perf_1ARNEImport_Delete(@Input ("ExpectedTime") long time, @Input ("ImportXML") String importXML, @Input ("DeleteXML") String deleteXML) {

    	
        DeleteCliOperator deleteOperator = (DeleteCliOperator) deleteProvider.provide(DeleteOperator.class);
               
        logger.info("++++++++++++++++++++++"+ time + importXML);     
        
        String fileToDeployName = importXML;
        String filePathinVapp = "/"+importXML;
        String deleteXMLToDeploy = deleteXML;
        String deleteXMLPathinVapp = "/"+deleteXML;
        
        deleteOperator.deployXMLFiletoServer(fileToDeployName,filePathinVapp );  
        deleteOperator.deployXMLFiletoServer(deleteXMLToDeploy,deleteXMLPathinVapp );
        logger.info("Setup completed successfully");
        int exitCode = deleteOperator.executeSetUp(importXML);
        
        long actualTime = deleteOperator.executeCommand(deleteXML);
	   
 	   	if (exitCode != 0){
 		   logger.info("Deletion failed");
 	   	}
 	   deleteOperator.executeTeardown(importXML,deleteXML);
 	   
        assertTrue("Delete Import not completed successfully", actualTime != -1);
        assertTrue("Delete execution time exceeded threshold", actualTime <= time);

    }  

}