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
        boolean result = logic.getGroups().stream()
                .map(connectedGroup -> new GroupEvaluationResult(connectedGroup.getGroup().evaluateChecks(data), connectedGroup.getConnectorToPreviousGroup()))
                .reduce(GroupResultCombiner::combineResults).map(GroupEvaluationResult::getPassed).orElse(false);

        if(logic.getGroups().size()>1)
        {
            logger.debug("evaluating logic {} - passed: [{}]", logic.getGroupConnectionLogic(), result);
        }
        else
        {
            logger.debug("evaluating logic - passed: [{}]", result);
        }
        return result;
    }


    //    public boolean evaluate(String name)
//    {
//        return getGroup(name).orElseThrow().getGroup().evaluateChecks();
//    }

}
