package com.datamelt.evaluate;

import com.datamelt.evaluate.check.GroupEvaluationResult;
import com.datamelt.evaluate.check.GroupResultCombiner;
import com.datamelt.evaluate.check.Logic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Evaluator
{
    private static final Logger logger = LoggerFactory.getLogger(Evaluator.class);

    public static <T> boolean evaluate(Logic<T> logic, T data)
    {
        return logic.getGroups().stream()
                .map(connectedGroup -> new GroupEvaluationResult(connectedGroup.getGroup().evaluateChecks(data), connectedGroup.getConnectorToPreviousGroup()))
                .reduce(GroupResultCombiner::combineResults).map(GroupEvaluationResult::getPassed).orElse(false);


    }
}
