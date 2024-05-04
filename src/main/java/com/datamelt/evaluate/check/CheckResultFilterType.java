package com.datamelt.evaluate.check;

import java.util.function.Predicate;

public enum CheckResultFilterType
{
    PASSED_ONLY,
    FAILED_ONLY,
    ALL;

    public static Predicate<CheckResult<?>> getFilter(CheckResultFilterType filter)
    {
        if(filter == PASSED_ONLY)
        {
            return CheckResult::getResult;
        }
        else if(filter == FAILED_ONLY)
        {
            return checkResult -> !checkResult.getResult();
        }
        else
        {
            return checkResult -> true;
        }
    }
}
