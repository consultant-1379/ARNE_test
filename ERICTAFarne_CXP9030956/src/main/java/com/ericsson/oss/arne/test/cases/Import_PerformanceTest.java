package com.ericsson.oss.arne.test.cases ;

import java.util.Calendar;
import java.util.Date;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.oss.arne.test.operators.ImportCliOperator;
import com.ericsson.oss.arne.test.operators.ImportOperator;

import org.apache.log4j.Logger;

import org.testng.annotations.Test;

import javax.inject.Inject;


public class Import_PerformanceTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<ImportOperator> importProvider;

    Logger logger = Logger.getLogger(Import_PerformanceTest.class);

    /**
     * @DESCRIPTION Import 1 RNC, 2 RXI , 400 RBS
     * @PRE
     * @PRIORITY HIGH
     */

    @TestId(id = "OSS-30375_Perf_1", title = "OSS-30375_Perf_1: ARNE Import")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB","VCDB","STCDB"})
    @DataDriven(name = "import_performancetest")
    public void oSS30375_Perf_1ARNEImport(@Input ("ExpectedTime") long time, @Input ("ImportXML") String importXML, @Input ("DeleteXML") String deleteXML ) {

        ImportCliOperator importOperator = (ImportCliOperator) importProvider.provide(ImportOperator.class);

        logger.info("++++++++++++++++++++++"+ time + importXML);

        String fileToDeployName = importXML;
        String filePathinVapp = "/"+importXML;
        String deleteXMLToDeploy = deleteXML;
        String deleteXMLPathinVapp = "/"+deleteXML;

        importOperator.deployXMLFiletoServer(fileToDeployName,filePathinVapp );
        importOperator.deployXMLFiletoServer(deleteXMLToDeploy,deleteXMLPathinVapp );
        logger.info("Setup completed successfully");
        long importtime= 0;
        long deletetime= 0;
        int loopCounter = 3;
        int sucessImportCounter = 0;
        int sucessDeleteCounter = 0;
        for (int execute = 1;execute <= loopCounter;execute ++) {

        long actualTime = importOperator.executeCommand(importXML);
/*
        int exitCode = importOperator.executeTearDown(deleteXML,importXML);
 	   time
 	   	if (exitCode != 0){
 		   logger.info("Teardown failed");
 	   	}
*/ 	    
        if(actualTime != -1){
         importtime += actualTime;
         sucessImportCounter ++;
         }
        assertTrue("Import not completed successfully", actualTime != -1);
        assertTrue("Import execution time exceeded threshold", actualTime <= time);
        try {
        	Thread.sleep(2700000);
        } catch(Exception ex) {
        	ex.printStackTrace();
        	
        }
        long actualTimeDelete = importOperator.executeCommand(deleteXML);
        if(actualTimeDelete != -1){
        	deletetime += actualTimeDelete;
        	sucessDeleteCounter ++;
        }
 	   	assertTrue("Delete Import not completed successfully", actualTimeDelete != -1);
 	   	assertTrue("Delete execution time exceeded threshold", actualTimeDelete <= time);
 	   	try { 
 	   		Thread.sleep(2700000);
 	   	} catch(Exception ex) {
 	   		ex.printStackTrace();
        }
        }
        try
        {
        	long avgImport=importtime/sucessImportCounter;
        	long avgDelete=deletetime/sucessDeleteCounter;
        	logger.info("Average Time for Import " + sucessImportCounter +" consecutive runs in HH:MM:SS - "+importOperator.calcToAcutalTime(avgImport));
        	logger.info("Average Time for Delete " + sucessDeleteCounter +" consecutive runs in HH:MM:SS - "+importOperator.calcToAcutalTime(avgDelete));
        } catch(Exception ex) {
 	   		ex.printStackTrace();
        }
    }
        
}

