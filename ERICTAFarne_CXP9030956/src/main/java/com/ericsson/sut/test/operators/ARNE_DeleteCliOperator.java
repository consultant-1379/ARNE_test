package com.ericsson.sut.test.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.tools.cli.CLIOperator;

import java.util.Map;

@Operator(context = Context.CLI)
public class ARNE_DeleteCliOperator extends CLIOperator implements ARNE_DeleteOperator {

    public Map<String, String> get(String step) {
        return loadData("ARNE_Delete_CliTestData.csv", step);
    }

}

