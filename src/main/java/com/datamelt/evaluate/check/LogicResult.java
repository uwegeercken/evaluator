package com.datamelt.evaluate.check;

import java.util.ArrayList;
import java.util.List;

public class LogicResult<T>
{
    private final List<GroupResult<T>> groupResults= new ArrayList<>();

    public void addGroupResult(GroupResult<T> groupResult)
    {
        groupResults.add(groupResult);
    }

    public List<GroupResult<T>> getGroupResults()
    {
        return groupResults;
    }

    public boolean getPassed()
    {
        boolean result = false;
        if(!groupResults.isEmpty())
        {
            result = groupResults.get(0).getPassed();
            for (int i = 1; i < groupResults.size(); i++)
            {
                result = combineResults(result, groupResults.get(i));
            }
        }
        return result;
    }

    private boolean combineResults(boolean groupResult1, GroupResult<T> groupResult2)
    {
        switch (groupResult2.getConnectorToPreviousGroup())
        {
            case AND ->
            {
                return groupResult2.getPassed() && groupResult1;
            }
            case OR ->
            {
                return groupResult2.getPassed() || groupResult1;
            }
            case NOT ->
            {
                return !(groupResult2.getPassed() && groupResult1);
            }
            case NOR ->
            {
                return !(groupResult2.getPassed() || groupResult1);
            }
            default -> throw new IllegalStateException("unexpected value: " + groupResult2.getConnectorToPreviousGroup());
        }
    }



}
