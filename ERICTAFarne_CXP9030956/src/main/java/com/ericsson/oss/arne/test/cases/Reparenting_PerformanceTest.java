package com.ericsson.oss.arne.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.exceptions.*;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.cifwk.taf.tools.cli.*;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

import java.util.*;

import com.ericsson.oss.arne.test.operators.ModifyCliOperator;
import com.ericsson.oss.arne.test.operators.ModifyOperator;
import com.ericsson.oss.arne.test.operators.ReparentingCliOperator;
import com.ericsson.oss.arne.test.operators.ReparentingOperator;
import com.ericsson.sut.test.operators.*;
import com.ericsson.sut.test.getters.*;

public class Reparenting_PerformanceTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<ReparentingOperator> reparentingProvider;

    Logger logger = Logger.getLogger(Reparenting_PerformanceTest.class); 
   
    /**
     * @DESCRIPTION Reparenting of RBS node
     * @PRE 
     * @PRIORITY HIGH
     */
         
    @TestId(id = "OSS-36823_Perf_1", title = "OSS-36823_Perf_1: ARNE Reparenting")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB","VCDB","STCDB"})
    @DataDriven(name = "reparenting_performancetest")
    public void oSS36823_Perf_1ARNEReparenting(@Input ("ImportXML") String importXML, @Input ("ReparentXML") String reparentXML, @Input ("DeleteRBSXML") String deleteRBSXML, @Input ("DeleteXML") String deleteXML ) {

    	ReparentingCliOperator modifyOperator = (ReparentingCliOperator) reparentingProvider.provide(ReparentingOperator.class);

        logger.info(importXML + "  " + reparentXML + " " + deleteRBSXML + " " + deleteXML);
        
        String fileToDeployName = importXML;
        String filePathinVapp = "/"+importXML;
        String reparentXMLToDeploy = reparentXML;
        String reparentXMLPathinVapp = "/"+reparentXML;
        String deleteRBSXMLToDeploy = deleteRBSXML;
        String deleteRBSXMLPathinVapp = "/"+deleteRBSXML;
        String deleteXMLToDeploy = deleteXML;
        String deleteXMLPathinVapp = "/"+deleteXML;
        
        modifyOperator.deployXMLFiletoServer(fileToDeployName,filePathinVapp ); 
        modifyOperator.deployXMLFiletoServer(reparentXMLToDeploy,reparentXMLPathinVapp);
        modifyOperator.deployXMLFiletoServer(deleteRBSXMLToDeploy,deleteRBSXMLPathinVapp );
        modifyOperator.deployXMLFiletoServer(deleteXMLToDeploy,deleteXMLPathinVapp );
        
        int setUp = modifyOperator.executeSetUp(importXML);      
        int exitCode_reparent = modifyOperator.executeCommand(reparentXML);
        int exitCode_deleteRBS = modifyOperator.executeCommand(deleteRBSXML);
        int tearDown = modifyOperator.executeTearDown(deleteXML);
     
        assertTrue("Setup not completed successfully", setUp == 0);
        assertTrue("Reparenting not completed successfully", exitCode_reparent == 0);
        assertTrue("RBS delete not completed successfully", exitCode_deleteRBS == 0);
        assertTrue("Teardown not completed successfully", tearDown == 0);
    }
}
	
