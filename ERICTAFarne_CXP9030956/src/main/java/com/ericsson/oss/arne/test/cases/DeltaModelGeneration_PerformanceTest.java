package com.ericsson.oss.arne.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.exceptions.*;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.oss.arne.test.operators.DeltaModelGenerationCliOperator;
import com.ericsson.oss.arne.test.operators.DeltaModelGenerationOperator;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

import java.util.*;


public class DeltaModelGeneration_PerformanceTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<DeltaModelGenerationOperator> deltaModelGenerationProvider;

    Logger logger = Logger.getLogger(DeltaModelGeneration_PerformanceTest.class);

     /**
     * @DESCRIPTION Execute DeltaModelGeneration Script for local and peer xmls
     * @PRE 
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-37091_Perf_1", title = "OSS-37091_Perf_1: ARNE DeltaModelGeneration")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB","VCDB","STCDB"})
    @DataDriven(name = "deltamodelgeneration_performancetest")
    public void oSS37091_Perf_1ARNEDeltaModelGeneration(@Input ("LocalXML") String localXML, @Input ("PeerXML") String peerXML) {

        DeltaModelGenerationCliOperator deltaModelGenerationOperator = (DeltaModelGenerationCliOperator) deltaModelGenerationProvider.provide(DeltaModelGenerationOperator.class);

        String localFileToDeploy = localXML;
        String localFilePathinVapp = "/"+localXML;
        String peerFileToDeploy = peerXML;
        String peerFilePathinVapp = "/"+peerXML;
        
        deltaModelGenerationOperator.deployXMLFiletoServer(localFileToDeploy,localFilePathinVapp ); 
        deltaModelGenerationOperator.deployXMLFiletoServer(peerFileToDeploy,peerFilePathinVapp ); 
        logger.info("Setup completed successfully");
        
        int exitcode = deltaModelGenerationOperator.executeDeltaModelGeneration(localXML,peerXML);
        deltaModelGenerationOperator.executeTeardown();
        
        assertTrue("Delta file not generated",exitcode == 0);
        
    }
}
	
