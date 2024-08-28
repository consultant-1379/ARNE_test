package com.ericsson.sut.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.exceptions.*;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.cifwk.taf.tools.cli.*;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

import java.util.*;

import com.ericsson.sut.test.operators.*;
import com.ericsson.sut.test.getters.*;

public class ARNE_Export_FunctionalTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<ARNE_ExportOperator> aRNE_ExportProvider;

    @Inject
    private ARNE_ExportGetter aRNE_ExportGetter;

    
    /**
     * @DESCRIPTION ARNE Export of Network Elements
     * @PRE ARNEServer should be online
     * @PRIORITY HIGH
     */
    @TestId(id = "TAFTEST-19_Func_11", title = "Exporting Network Element")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    public void exportingNetworkElement() {

        ARNE_ExportCliOperator aRNE_ExportOperator = (ARNE_ExportCliOperator) aRNE_ExportProvider.provide(ARNE_ExportOperator.class);
        
        CLI cli;
    	Shell shell;
    	
    	
    	Logger logger = Logger.getLogger( ARNE_ImportCliOperator.class);
        
        setTestInfo("CD_export");
        Map<String, String> step1 = aRNE_ExportOperator.get("TAFTEST-19_Func_11_Step1");
        CLIOperator.Result result1 = aRNE_ExportOperator.execute("CD_export", step1);
        setTestStep("STDOUT As Expected");
        assertTrue(result1.getStdOut().contains(step1.get("STDOUT")));

        setTestInfo("EXPORT_XML");
        Map<String, String> step2 = aRNE_ExportOperator.get("TAFTEST-19_Func_11_Step2");
        CLIOperator.Result result2 = aRNE_ExportOperator.execute("EXPORT_XML", step2);
        setTestStep("STDOUT As Expected");
        assertTrue(result2.getStdOut().contains(step2.get("STDOUT")));

    }
}
	
