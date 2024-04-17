package com.datamelt.evaluate.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class Check<T>
{
    private static final Logger logger = LoggerFactory.getLogger(Check.class);
    private final String name;
    private final Function<T,Boolean> check;

    public Check(String name, Function<T,Boolean> check)
    {
        this.name = name;
        this.check = check;
    }

    public String getName()
    {
        return name;
    }

    public boolean evaluate(T dataObject)
    {
        boolean result = check.apply(dataObject);
        //logger.debug("checking value [{}] against value [{}] using check [{}] --> [{}]", value1.toString(), value2.toString(), name, result);
        return result;
    }

}
