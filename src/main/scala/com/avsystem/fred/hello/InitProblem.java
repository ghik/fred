package com.avsystem.fred.hello;

public class InitProblem {
    public InitProblem() {
        init();
    }

    public void init() {
    }

    public static void main(String[] args) {
        new SubInitProblem();
    }
}

class SubInitProblem extends InitProblem {
    private String costam = "costam";

    @Override
    public void init() {
        System.out.println(costam);
    }
}
