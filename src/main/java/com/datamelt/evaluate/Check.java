package com.datamelt.evaluate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

public class Check<T,U>
{
    private static final Logger logger = LoggerFactory.getLogger(Check.class);
    private final String name;
    private final T value1;
    private final U value2;
    private final BiFunction<T,U,Boolean> check;

    public Check(String name, T value1, U value2, BiFunction<T,U,Boolean> check)
    {
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
        this.check = check;
    }

    public String getName()
    {
        return name;
    }

    public boolean evaluate()
    {
        boolean result = check.apply(value1, value2);
        logger.debug("checking value [{}] against value [{}] using check [{}] --> [{}]", value1.toString(), value2.toString(), name, result);
        return result;
    }

    public T getValue1()
    {
        return value1;
    }

    public U getValue2()
    {
        return value2;
    }
}
