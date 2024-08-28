package com.ericsson.oss.arne.test.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;

import javax.inject.Singleton;

@Operator(context = Context.API)
@Singleton
public class CalculatorOperatorAPI implements CalculatorOperator {

    public static final String CALCULATOR_API_NAME = "API";

    @Override
    public String getName() {
        return CALCULATOR_API_NAME;
    }


    @Override
    public int sum(int x, int y) {
        return x + y;
    }

    @Override
    public int subtraction(int x, int y) {
        return x - y;
    }

    @Override
    public int divide(int x, int y) {
        return x / y;
    }

    @Override
    public int multiply(int x, int y) {
        return x * y;
    }
}
