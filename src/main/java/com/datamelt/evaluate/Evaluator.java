package com.datamelt.evaluate;

import com.datamelt.evaluate.check.EvaluationResult;
import com.datamelt.evaluate.check.Group;
import com.datamelt.evaluate.check.GroupResult;
import com.datamelt.evaluate.check.Logic;

public class Evaluator
{
    public static <T> EvaluationResult<T> evaluate(Logic<T> logic, T data)
    {
        EvaluationResult<T> evaluationResult = new EvaluationResult<>();
        for(Group<T> group : logic.getGroups())
        {
            evaluationResult.addGroupResult(new GroupResult<>(group, data));
        }
        return evaluationResult;
    }
}
