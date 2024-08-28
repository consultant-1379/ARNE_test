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

public class ARNE_Modify_FunctionalTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<ARNE_ModifyOperator> aRNE_ModifyProvider;

    @Inject
    private ARNE_ModifyGetter aRNE_ModifyGetter;

    @TestId(id = "datadriven", title = "DataDriven Example")
    @Test
    @DataDriven(name = "arne_modify_functionaltest")
    public void shouldBePopulatedFromCsv(@Input("first") int x, @Input("second") int y, @Output("result") int expected) {
          assertEquals(x + y, expected);
    }


    /**
     * @DESCRIPTION ARNE Modification of Network Elements
     * @PRE ARNEServer should be online and the network elements to be modified should be present
     * @PRIORITY HIGH
     */
    @TestId(id = "TAFTEST-19_Func_12", title = "Modify Network Element")
    @Context(context = {Context.CLI})
    @Test(groups={"Feature Regression"})
    public void modifyNetworkElement() {

        ARNE_ModifyCliOperator aRNE_ModifyOperator = (ARNE_ModifyCliOperator) aRNE_ModifyProvider.provide(ARNE_ModifyOperator.class);

        setTestInfo("CD_modify");
        Map<String, String> step1 = aRNE_ModifyOperator.get("TAFTEST-19_Func_12_Step1");
        CLIOperator.Result result1 = aRNE_ModifyOperator.execute("CD_modify", step1);
        setTestStep("STDOUT As Expected");
        assertTrue(result1.getStdOut().contains(step1.get("STDOUT")));

        setTestInfo("VALIDATE_MODIFY_XML");
        Map<String, String> step2 = aRNE_ModifyOperator.get("TAFTEST-19_Func_12_Step2");
        CLIOperator.Result result2 = aRNE_ModifyOperator.execute("VALIDATE_MODIFY_XML", step2);
        setTestStep("STDOUT As Expected");
        assertTrue(result2.getStdOut().contains(step2.get("STDOUT")));

        setTestInfo("IMPORT_MODIFY_XML");
        Map<String, String> step3 = aRNE_ModifyOperator.get("TAFTEST-19_Func_12_Step3");
        CLIOperator.Result result3 = aRNE_ModifyOperator.execute("IMPORT_MODIFY_XML", step3);
        setTestStep("STDOUT As Expected");
        assertTrue(result3.getStdOut().contains(step3.get("STDOUT")));

    }
}
	
