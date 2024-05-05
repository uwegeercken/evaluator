package com.datamelt.evaluate;

import com.datamelt.evaluate.check.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Evaluator
{
    public static <T> LogicResult<T> evaluate(Logic<T> logic, T data)
    {
        LogicResult<T> logicResult = new LogicResult<>();
        for(Group<T> group : logic.getGroups())
        {
            logicResult.addGroupResult(new GroupResult<>(group, data));
        }
        return logicResult;
    }
}
