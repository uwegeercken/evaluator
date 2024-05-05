package com.datamelt.evaluate.check;

import java.util.List;

public class GroupResult<T>
{
    private final String name;
    private final ConnectorType connectorTypeChecks;
    private final ConnectorType connectorTypePreviousGroup;
    private final List<CheckResult> checkResults;
    private final boolean passed;

    public GroupResult(Group<T> group, T dataObject)
    {
        this.name = group.getName();
        this.connectorTypeChecks = group.getConnectorTypeChecks();
        this.connectorTypePreviousGroup = group.getConnectorTypePreviousGroup();
        this.checkResults = group.getCheckResults(dataObject);
        this.passed = getCombinedChecksResults(checkResults);
    }

    public boolean passed()
    {
        return passed;
    }

    public ConnectorType getConnectorToPreviousGroup()
    {
        return connectorTypePreviousGroup;
    }

    public ConnectorType getConnectorTypePreviousGroup()
    {
        return connectorTypePreviousGroup;
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
        switch (connectorTypeChecks)
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
                        .reduce((result1, result2) -> (result1 || result2)).orElse(true);
                return !result;
            }
            case NOT ->
            {
                boolean result = results.stream()
                        .map(CheckResult::passed)
                        .reduce((result1, result2) -> (result1 && result2)).orElse(true);
                return !result;
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
