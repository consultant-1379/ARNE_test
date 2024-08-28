package com.ericsson.sut.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.exceptions.*;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.cifwk.taf.tools.cli.*;

import org.testng.annotations.Test;
import javax.inject.Inject;
import java.util.*;

import com.ericsson.sut.test.operators.*;
import com.ericsson.sut.test.getters.*;

public class ARNE_Import_FunctionalTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<ARNE_ImportOperator> aRNE_ImportProvider;

    @Inject
    private ARNE_ImportGetter aRNE_ImportGetter;

    @TestId(id = "datadriven", title = "DataDriven Example")
    @Context(context = {Context.CLI})
    @Test
    
    @DataDriven(name = "arne_import_functionaltest")
    public void shouldBePopulatedFromCsv( @Input("host") String host, @Input("commandRef") String commandRef,@Input("args") String argument, @Output("expectedOut") String expectedOut) {
    	ARNE_ImportOperator aRNE_ImportOperator = aRNE_ImportProvider.provide(ARNE_ImportOperator.class);
    	System.out.println("-----inside shuldbepopulatedfromcvs----");
    	String result =   aRNE_ImportOperator.executeCommand(host, commandRef,argument)  ;
    	assertTrue(result.contains(expectedOut)); 
    			}
    }
    

    /**
     * @DESCRIPTION ARNE Import of Network Elements
     * @PRE ARNEServer should be online
     * @PRIORITY HIGH
     */
   /* @TestId(id = "TAFTEST-19_Func_10", title = "Importing Network Element")
    @Context(context = {Context.CLI})
    @Test(groups={"Feature Regression"})
    public void importingNetworkElement() {

        ARNE_ImportCliOperator aRNE_ImportOperator = (ARNE_ImportCliOperator) aRNE_ImportProvider.provide(ARNE_ImportOperator.class);

        setTestInfo("CD_import");
        Map<String, String> step1 = aRNE_ImportOperator.get("TAFTEST-19_Func_10_Step1");
        CLIOperator.Result result1 = aRNE_ImportOperator.execute("CD_import", step1);
        setTestStep("STDOUT As Expected");
        assertTrue(result1.getStdOut().contains(step1.get("STDOUT")));

        setTestInfo("VALIDATE_XML");
        Map<String, String> step2 = aRNE_ImportOperator.get("TAFTEST-19_Func_10_Step2");
        CLIOperator.Result result2 = aRNE_ImportOperator.execute("VALIDATE_XML", step2);
        setTestStep("STDOUT As Expected");
        assertTrue(result2.getStdOut().contains(step2.get("STDOUT")));

        setTestInfo("IMPORT_XML");
        Map<String, String> step3 = aRNE_ImportOperator.get("TAFTEST-19_Func_10_Step3");
        CLIOperator.Result result3 = aRNE_ImportOperator.execute("IMPORT_XML", step3);
        setTestStep("STDOUT As Expected");
        assertTrue(result3.getStdOut().contains(step3.get("STDOUT")));

    }
}*/
	
