package com.datamelt.evaluate;

import com.datamelt.evaluate.check.ConnectedGroup;
import com.datamelt.evaluate.check.GroupEvaluationResult;
import com.datamelt.evaluate.check.GroupResultCombiner;
import com.datamelt.evaluate.check.Logic;
import com.datamelt.evaluate.model.LogicProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Evaluator
{
    private static final Logger logger = LoggerFactory.getLogger(Evaluator.class);

    public static <T> boolean evaluate(LogicProvider<T> provider, T data)
    {
        return evaluate(provider.mapValues(data));
    }

    private static boolean evaluate(Logic logic)
    {
        List<String> connectors = logic.getGroups().stream()
                .skip(1)
                .map(ConnectedGroup::toString)
                .toList();

        boolean result = logic.getGroups().stream()
                .map(connectedGroup -> new GroupEvaluationResult(connectedGroup.getGroup().evaluateChecks(), connectedGroup.getConnectorToPreviousGroup()))
                .reduce(GroupResultCombiner::combineResults).map(GroupEvaluationResult::getPassed).orElse(false);

        logger.debug("results of all groups using connector to previous group {} --> [{}]", connectors, result);
        return result;
    }

    //    public boolean evaluate(String name)
//    {
//        return getGroup(name).orElseThrow().getGroup().evaluateChecks();
//    }

}
