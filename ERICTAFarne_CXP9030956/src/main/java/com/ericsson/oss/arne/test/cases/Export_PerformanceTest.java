package com.ericsson.oss.arne.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

import com.ericsson.oss.arne.test.operators.ExportCliOperator;
import com.ericsson.oss.arne.test.operators.ExportOperator;

public class Export_PerformanceTest extends TorTestCaseHelper implements TestCase  {

	private static Logger logger = Logger.getLogger(Export_PerformanceTest.class.getName());
	@Inject
	private OperatorRegistry<ExportOperator> exportProvider;
  
    /**
     * @DESCRIPTION Exports all the network elements
     * @PRE 
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-26825_Perf_1", title = "ARNE Export")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB","VCDB","STCDB"})
    @DataDriven(name = "Export_PerformanceTest")
    
    public void aRNEExport(@Input ("ExpectedTime") long time) throws Exception {

        ExportCliOperator exportOperator = (ExportCliOperator) exportProvider.provide(ExportOperator.class);

        setTestInfo("Call export.sh script");
        setTestInfo("Time"  + " " +time);
        long exporttime= 0;
        int loopCounter = 5;
        int sucessExportCounter = 0;
        for (int execute = 1;execute <= loopCounter;execute ++) {
        long actualTime = exportOperator.executeCommand();
        logger.info("Actual execution time"+actualTime);
        if(actualTime != -1){
        	exporttime += actualTime;
        	sucessExportCounter ++;
            }
        int nodesinXML = exportOperator.getNumberofNodesInXML();
        int nodesinCS = exportOperator.getNumberofNodesFromCS();
		
		logger.info("Number of Nodes in XML "+ nodesinXML);
        logger.info("Number of Nodes in CS "+ nodesinCS);
        
        assertTrue("Export not completed", actualTime != -1);
        assertTrue("Number of Nodes in CS and XML are not equal", nodesinXML == nodesinCS);
        assertTrue("Export execution time exceeded threshold", actualTime <= time);
        try { 
        	Thread.sleep(600000);
 	   	} catch(Exception ex) {
 	   		ex.printStackTrace();
        }
        }
        try
        {
        	long avgExport=exporttime/sucessExportCounter;
        	logger.info("Average Time for Export " + sucessExportCounter +" consecutive runs in HH:MM:SS - "+exportOperator.calcToAcutalTime(avgExport));
        } catch(Exception ex) {
 	   		ex.printStackTrace();
        }
    }
}
	
