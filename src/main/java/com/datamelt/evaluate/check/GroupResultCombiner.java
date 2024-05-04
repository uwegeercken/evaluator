package com.datamelt.evaluate.check;

public class GroupResultCombiner
{
    public static GroupResult combineResults(GroupResult groupResult1, GroupResult groupResult2)
    {
        switch (groupResult2.getConnectorType())
        {
            case AND ->
            {
                return new GroupResult(groupResult2.getPassed() && groupResult1.getPassed(), groupResult2.getConnectorType());
            }
            case OR ->
            {
                return new GroupResult(groupResult2.getPassed() || groupResult1.getPassed(), groupResult2.getConnectorType());
            }
            case NOT ->
            {
                return new GroupResult(!(groupResult2.getPassed() && groupResult1.getPassed()), groupResult2.getConnectorType());
            }
            case NOR ->
            {
                return new GroupResult(!(groupResult2.getPassed() || groupResult1.getPassed()), groupResult2.getConnectorType());
            }
            default -> throw new IllegalStateException("unexpected value: " + groupResult2.getConnectorType());
        }
    }
}
