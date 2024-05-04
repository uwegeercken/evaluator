package com.datamelt.evaluate;

import com.datamelt.evaluate.check.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Evaluator
{
    public static <T> boolean evaluate(Logic<T> logic, T data)
    {
        return logic.getGroups().stream()
                .map(connectedGroup -> new GroupResult(connectedGroup.getGroup().evaluateChecks(data), connectedGroup.getConnectorToPreviousGroup()))
                .reduce(GroupResultCombiner::combineResults).map(GroupResult::getPassed).orElse(false);
    }

    public static <T> Map<String,List<String>> test(Logic<T> logic, T data, CheckResultFilterType filterType)
    {
        return logic.getGroups().stream()
                .map(ConnectedGroup::getGroup)
                .collect(Collectors.toMap(Group::getName, group -> test(group, data, filterType)));
    }

    public static <T> boolean evaluate(Group<T> group, T data)
    {
        return group.evaluateChecks(data);
    }

    public static <T> List<String> test(Group<T> group, T data, CheckResultFilterType filterType)
    {
        return group.test(data, filterType);
    }
}
