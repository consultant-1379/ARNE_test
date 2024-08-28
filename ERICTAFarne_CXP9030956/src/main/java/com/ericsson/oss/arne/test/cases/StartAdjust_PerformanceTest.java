package com.ericsson.oss.arne.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

import com.ericsson.oss.arne.test.operators.StartAdjustCliOperator;
import com.ericsson.oss.arne.test.operators.StartAdjustOperator;

public class StartAdjust_PerformanceTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<StartAdjustOperator> startAdjustProvider;

    Logger logger = Logger.getLogger(StartAdjust_PerformanceTest.class);
    
    /**
     * @DESCRIPTION Execute StartAdjust script
     * @PRE 
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-37090_Perf_1", title = "OSS-37090_Perf_1: ARNE StartAdjust")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB","VCDB","STCDB"})
    @DataDriven(name = "startadjust_performancetest")
    public void oSS37090_Perf_1ARNEStartAdjust(@Input ("ImportXML") String importXML, @Input ("DeleteXML") String deleteXML) {

        StartAdjustCliOperator startAdjustOperator = (StartAdjustCliOperator) startAdjustProvider.provide(StartAdjustOperator.class);

        logger.info("Import XML is " + importXML);
        
        String fileToDeployName = importXML;
        String filePathinVapp = "/"+importXML;
        String fileToDeployNamedelete = deleteXML;
        String filePathinVappdelete = "/"+deleteXML;
        
        startAdjustOperator.deployXMLFiletoServer(fileToDeployName,filePathinVapp ); 
        startAdjustOperator.deployXMLFiletoServer(fileToDeployNamedelete,filePathinVappdelete );
        logger.info("Setup completed successfully");
        
        int exitCode_MAFoffline = startAdjustOperator.executeImportwithMAFOffline(importXML);
        int exitCode_SA = startAdjustOperator.executeStartAdjustwithMAFOnline();
        int exitCode_segMCS = startAdjustOperator.checkSeg_masterservice_CS_for_importedNodes();
        int exitCode_MAFoffline_delete = startAdjustOperator.executeImportwithMAFOffline(deleteXML);
        int exitCode_SA_delete = startAdjustOperator.executeStartAdjustwithMAFOnline();
        int exitCode_segMCS_delete = startAdjustOperator.checkSeg_masterservice_CS_for_deletedNodes();
        
        assertTrue("MAF not made offline", exitCode_MAFoffline == 0);
        assertTrue("start Adjust not completed", exitCode_SA == 0);
        assertTrue("Managed Element not added to SegMasterCS", exitCode_segMCS == 0);
        assertTrue("MAF not made offline for delete", exitCode_MAFoffline_delete == 0);
        assertTrue("start Adjust not completed", exitCode_SA_delete == 0);
        assertTrue("Managed Element not removed from SegMasterCS", exitCode_segMCS_delete == 0);

    }
}
	
