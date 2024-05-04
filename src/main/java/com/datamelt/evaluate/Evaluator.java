package com.datamelt.evaluate;

import com.datamelt.evaluate.check.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Evaluator
{
    public static <T> boolean evaluate(Logic<T> logic, T data)
    {
        return logic.getGroups().stream()
                .map(connectedGroup -> new GroupEvaluationResult(connectedGroup.getGroup().evaluateChecks(data), connectedGroup.getConnectorToPreviousGroup()))
                .reduce(GroupResultCombiner::combineResults).map(GroupEvaluationResult::getPassed).orElse(false);
    }

    public static <T> boolean evaluate(Group<T> group, T data)
    {
        return group.evaluateChecks(data);
    }
}
