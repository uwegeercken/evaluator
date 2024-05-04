package com.datamelt.evaluate.check;

public class CheckResult<T>
{
    private final Check<T> check;
    private final boolean result;

    public CheckResult(Check<T> check, boolean result)
    {
        this.check = check;
        this.result = result;
    }

    public Check<T> getCheck()
    {
        return check;
    }

    public boolean getResult()
    {
        return result;
    }

    @Override
    public String toString()
    {
        return "check: [" + check.getName() + "] - passed: " + result;
    }
}
