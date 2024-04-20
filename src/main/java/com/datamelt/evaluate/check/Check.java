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

    public boolean evaluate(T value)
    {
        boolean result = check.test(value);
        //logger.debug("checking value [{}] against value [{}] using check [{}] --> [{}]", value1.toString(), value2.toString(), name, result);
        return result;
    }

}
