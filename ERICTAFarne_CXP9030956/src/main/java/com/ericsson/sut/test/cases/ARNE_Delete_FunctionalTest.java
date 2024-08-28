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

public class ARNE_Delete_FunctionalTest extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<ARNE_DeleteOperator> aRNE_DeleteProvider;

    @Inject
    private ARNE_DeleteGetter aRNE_DeleteGetter;

    @TestId(id = "datadriven", title = "DataDriven Example")
    @Test
    @DataDriven(name = "arne_delete_functionaltest")
    public void shouldBePopulatedFromCsv(@Input("first") int x, @Input("second") int y, @Output("result") int expected) {
          assertEquals(x + y, expected);
    }


    /**
     * @DESCRIPTION ARNE Deletion of Network Elements
     * @PRE ARNEServer should be online and the network elements to be deleted should be present
     * @PRIORITY HIGH
     */
    @TestId(id = "TAFTEST-19_Func_13", title = "Delete Network Element")
    @Context(context = {Context.CLI})
    @Test(groups={"Feature Regression"})
    public void deleteNetworkElement() {

        ARNE_DeleteCliOperator aRNE_DeleteOperator = (ARNE_DeleteCliOperator) aRNE_DeleteProvider.provide(ARNE_DeleteOperator.class);

        setTestInfo("CD_delete");
        Map<String, String> step1 = aRNE_DeleteOperator.get("TAFTEST-19_Func_13_Step1");
        CLIOperator.Result result1 = aRNE_DeleteOperator.execute("CD_delete", step1);
        setTestStep("STDOUT As Expected");
        assertTrue(result1.getStdOut().contains(step1.get("STDOUT")));

        setTestInfo("VALIDATE_DELETE_XML");
        Map<String, String> step2 = aRNE_DeleteOperator.get("TAFTEST-19_Func_13_Step2");
        CLIOperator.Result result2 = aRNE_DeleteOperator.execute("VALIDATE_DELETE_XML", step2);
        setTestStep("STDOUT As Expected");
        assertTrue(result2.getStdOut().contains(step2.get("STDOUT")));

        setTestInfo("IMPORT_DELETE_XML");
        Map<String, String> step3 = aRNE_DeleteOperator.get("TAFTEST-19_Func_13_Step3");
        CLIOperator.Result result3 = aRNE_DeleteOperator.execute("IMPORT_DELETE_XML", step3);
        setTestStep("STDOUT As Expected");
        assertTrue(result3.getStdOut().contains(step3.get("STDOUT")));

    }
}
	
