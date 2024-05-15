package com.datamelt.evaluate.check;

import java.util.List;

public class GroupResult<T>
{
    private final String name;
    private final GroupConnectorType groupConnectorTypeChecks;
    private final GroupConnectorType groupConnectorTypePreviousGroup;
    private final List<CheckResult> checkResults;
    private final boolean passed;

    public GroupResult(Group<T> group, T dataObject)
    {
        this.name = group.getName();
        this.groupConnectorTypeChecks = group.getConnectorTypeChecks();
        this.groupConnectorTypePreviousGroup = group.getConnectorTypePreviousGroup();
        this.checkResults = group.getCheckResults(dataObject);
        this.passed = getCombinedChecksResults(checkResults);
    }

    public boolean passed()
    {
        return passed;
    }

    public GroupConnectorType getConnectorToPreviousGroup()
    {
        return groupConnectorTypePreviousGroup;
    }

    public GroupConnectorType getConnectorTypePreviousGroup()
    {
        return groupConnectorTypePreviousGroup;
    }

    public String getName()
    {
        return name;
    }

    public List<CheckResult> getCheckResults()
    {
        return checkResults;
    }

    private boolean getCombinedChecksResults(List<CheckResult> results)
    {
        switch (groupConnectorTypeChecks)
        {
            case OR ->
            {
                return results.stream()
                        .map(CheckResult::passed)
                        .reduce((result1, result2) -> result1 || result2).orElse(false);
            }
            case NOR ->
            {
                boolean result =  results.stream()
                        .map(CheckResult::passed)
                        .reduce((result1, result2) -> (result1 || result2)).orElse(false);
                return !result;
            }
            case NOT ->
            {
                boolean result = results.stream()
                        .map(CheckResult::passed)
                        .reduce((result1, result2) -> (result1 && !result2)).orElse(false);
                return result;
            }
            default ->
            {
                return results.stream()
                        .map(CheckResult::passed)
                        .reduce((result1, result2) -> result1 && result2).orElse(false);
            }
        }
    }
}
