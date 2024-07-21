package com.datamelt.evaluate.check;

import java.util.List;

public class EvaluationResult<T>
{
    private final List<GroupResult<T>> groupResults;
    private boolean passed;

    public EvaluationResult(List<GroupResult<T>> groupResults)
    {
        this.groupResults = groupResults;
        this.evaluate();
    }

    public List<GroupResult<T>> getGroupResults()
    {
        return groupResults;
    }

    public List<CheckResult> getCheckResults(String groupName, CheckResultFilterType checkResultFilter)
    {
        return getGroupResult(groupName).getCheckResults(checkResultFilter);
    }

    public long getTotalNumberOfCheckResults(CheckResultFilterType checkResultFilter)
    {
        return groupResults.stream()
                .map(groupResult -> groupResult.getNumberOfCheckResults(checkResultFilter))
                .reduce(0L,Long::sum);
    }

    public long getTotalNumberOfCheckResults()
    {
        return groupResults.stream()
                .map(GroupResult::getNumberOfCheckResults)
                .reduce(0L,Long::sum);
    }

    public GroupResult<T> getGroupResult(String groupName)
    {
        return groupResults
                .stream()
                .filter(result -> result.getName().equals(groupName))
                .findAny().orElseThrow(()-> new RuntimeException("the specified group [" + groupName + "] was not found"));
    }

    public boolean passed()
    {
        return passed;
    }

    public boolean failed()
    {
        return !passed;
    }

    private void evaluate()
    {
        boolean result = false;
        if(!groupResults.isEmpty())
        {
            result = groupResults.get(0).passed();
            for (int i = 1; i < groupResults.size(); i++)
            {
                result = combineResults(result, groupResults.get(i));
            }
        }
        passed = result;
    }

    private boolean combineResults(boolean intermediateResult, GroupResult<T> groupResult2)
    {
        switch (groupResult2.getConnectorToPreviousGroup())
        {
            case AND ->
            {
                return groupResult2.passed() && intermediateResult;
            }
            case OR ->
            {
                return groupResult2.passed() || intermediateResult;
            }
            case AND_NOT ->
            {
                return !groupResult2.passed() && intermediateResult;
            }
            case OR_NOT ->
            {
                return !groupResult2.passed() || intermediateResult;
            }
            default -> throw new IllegalStateException("unexpected value: " + groupResult2.getConnectorToPreviousGroup());
        }
    }
}
