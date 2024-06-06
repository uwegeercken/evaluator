package com.datamelt.evaluate.check;

import java.util.List;

public class GroupResult<T>
{
    private final String name;
    private final CheckConnectorType checkConnectorType;
    private final GroupConnectorType groupConnectorTypePreviousGroup;
    private final List<CheckResult> checkResults;
    private final boolean passed;

    public GroupResult(Group<T> group, T dataObject)
    {
        this.name = group.getName();
        this.checkConnectorType = group.getCheckConnectorType();
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

    public List<CheckResult> getCheckResults(CheckResultFilterType checkResultFilter)
    {
        return checkResults.stream()
                .filter(CheckResultFilterType.getFilter(checkResultFilter))
                .toList();
    }

    public long getNumberOfCheckResults()
    {
        return checkResults.size();
    }

    public long getNumberOfCheckResults(CheckResultFilterType checkResultFilter)
    {
        return checkResults
                .stream()
                .filter(CheckResultFilterType.getFilter(checkResultFilter))
                .toList().size();
    }

    private boolean getCombinedChecksResults(List<CheckResult> results)
    {
        switch (checkConnectorType)
        {
            case OR ->
            {
                return results.stream()
                        .map(CheckResult::passed)
                        .reduce((result1, result2) -> result1 || result2).orElse(false);
            }
            default ->
            {
                return results.stream()
                        .map(CheckResult::passed)
                        .reduce((result1, result2) -> result1 && result2).orElse(false);
            }
        }
    }

    @Override
    public String toString()
    {
        return "group: [" + name + "] - passed [" + passed + "]";
    }
}
