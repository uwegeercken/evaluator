package com.datamelt.evaluate.check;

import java.util.List;

public class GroupResult<T>
{
    private final Group<T> group;
    private final List<CheckResult<T>> checkResults;
    private final boolean passed;

    public GroupResult(Group<T> group, T dataObject)
    {
        this.group = group;
        this.checkResults = group.getCheckResults(dataObject);
        this.passed = getCombinedChecksResults(checkResults);
    }

    public boolean getPassed()
    {
        return passed;
    }

    public ConnectorType getConnectorToPreviousGroup()
    {
        return group.getConnectorTypePreviousGroup();
    }

    public Group<T> getGroup()
    {
        return group;
    }

    public List<CheckResult<T>> getCheckResults()
    {
        return checkResults;
    }

    private boolean getCombinedChecksResults(List<CheckResult<T>> results)
    {
        switch (group.getConnectorTypeChecks())
        {
            case OR ->
            {
                return results.stream()
                        .map(CheckResult::getResult)
                        .reduce((result1, result2) -> result1 || result2).orElse(false);
            }
            case NOR ->
            {
                boolean result =  results.stream()
                        .map(CheckResult::getResult)
                        .reduce((result1, result2) -> (result1 || result2)).orElse(true);
                return !result;
            }
            case NOT ->
            {
                boolean result = results.stream()
                        .map(CheckResult::getResult)
                        .reduce((result1, result2) -> (result1 && result2)).orElse(true);
                return !result;
            }
            default ->
            {
                return results.stream()
                        .map(CheckResult::getResult)
                        .reduce((result1, result2) -> result1 && result2).orElse(false);
            }
        }
    }
}
