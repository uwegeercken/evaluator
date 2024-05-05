package com.datamelt.evaluate.check;

public class CheckResult
{
    private final String name;
    private final boolean passed;

    public CheckResult(String name, boolean passed)
    {
        this.name = name;
        this.passed = passed;
    }

    public String getName()
    {
        return name;
    }

    public boolean passed()
    {
        return passed;
    }

    @Override
    public String toString()
    {
        return "check: [" + name + "] - passed: " + passed;
    }
}
