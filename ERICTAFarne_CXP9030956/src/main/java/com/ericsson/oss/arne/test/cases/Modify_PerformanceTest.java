package com.ericsson.oss.arne.test.cases;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

import com.ericsson.oss.arne.test.operators.ModifyCliOperator;
import com.ericsson.oss.arne.test.operators.ModifyOperator;

public class Modify_PerformanceTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<ModifyOperator> modifyProvider;
    
    Logger logger = Logger.getLogger(Modify_PerformanceTest.class);

    /**
     * @DESCRIPTION Modify 1 RNC, 2 RXI , 400 RBS
     * @PRE 
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-35691_Perf_1", title = "ARNE Import Modify")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB","VCDB","STCDB"})
    @DataDriven(name = "modify_performancetest")
    public void oSS33022_Perf_1ARNEImport_Modify(@Input ("ImportXML") String importXML, @Input ("ModifyXML") String modifyXML, @Input ("DeleteXML") String deleteXML) {

        ModifyCliOperator modifyOperator = (ModifyCliOperator) modifyProvider.provide(ModifyOperator.class);

        logger.info(importXML + "  " + modifyXML + " " + deleteXML);
        
        String fileToDeployName = importXML;
        String filePathinVapp = "/"+importXML;
        String modifyXMLToDeploy = modifyXML;
        String modifyXMLPathinVapp = "/"+modifyXML;
        String deleteXMLToDeploy = deleteXML;
        String deleteXMLPathinVapp = "/"+deleteXML;
        
        modifyOperator.deployXMLFiletoServer(fileToDeployName,filePathinVapp ); 
        modifyOperator.deployXMLFiletoServer(modifyXMLToDeploy,modifyXMLPathinVapp);
        modifyOperator.deployXMLFiletoServer(deleteXMLToDeploy,deleteXMLPathinVapp );
        
        int setUp = modifyOperator.executeSetUp(importXML);      
        int exitCode = modifyOperator.executeCommand(modifyXML);
        int tearDown = modifyOperator.executeTearDown(importXML,modifyXML,deleteXML);
    
        assertTrue("Setup not completed successfully", setUp == 0);
        assertTrue("Modify Import not completed successfully", exitCode == 0);
        assertTrue("Teardown not completed successfully", tearDown == 0);
    }
}
	
