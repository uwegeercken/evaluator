package com.datamelt.evaluate.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class Check<T>
{
    private static final Logger logger = LoggerFactory.getLogger(Check.class);
    private final String name;
    private final Predicate<T> check;

    public Check(String name, Predicate<T> predicate)
    {
        this.name = name;
        this.check = predicate;
    }

    public String getName()
    {
        return name;
    }

    public boolean evaluate(T dataObject)
    {
        boolean result = check.test(dataObject);
        logger.trace("evaluating check [{}] against value [{}], passed: [{}]", dataObject.toString(), name, result);
        return result;
    }

}
