package com.datamelt.evaluate.check;

public class GroupResultCombiner
{
    public static GroupEvaluationResult combineResults(GroupEvaluationResult groupEvaluationResult1, GroupEvaluationResult groupEvaluationResult2)
    {
        switch (groupEvaluationResult2.getConnectorType())
        {
            case AND ->
            {
                return new GroupEvaluationResult(groupEvaluationResult2.getPassed() && groupEvaluationResult1.getPassed(), groupEvaluationResult2.getConnectorType());
            }
            case OR ->
            {
                return new GroupEvaluationResult(groupEvaluationResult2.getPassed() || groupEvaluationResult1.getPassed(), groupEvaluationResult2.getConnectorType());
            }
            case NOT ->
            {
                return new GroupEvaluationResult(!(groupEvaluationResult2.getPassed() && groupEvaluationResult1.getPassed()), groupEvaluationResult2.getConnectorType());
            }
            case NOR ->
            {
                return new GroupEvaluationResult(!(groupEvaluationResult2.getPassed() || groupEvaluationResult1.getPassed()), groupEvaluationResult2.getConnectorType());
            }
            default -> throw new IllegalStateException("unexpected value: " + groupEvaluationResult2.getConnectorType());
        }
    }
}
