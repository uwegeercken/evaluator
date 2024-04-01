package com.datamelt.evaluate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

public class Check<T,U>
{
    private static final Logger logger = LoggerFactory.getLogger(Check.class);
    private final String name;
    private final Field<T> field1;
    private final Field<U> field2;
    private final BiFunction<Field<T>, Field<U>, Boolean> check;

    public Check(String name, Field<T> field1, Field<U> field2, BiFunction<Field<T>, Field<U>, Boolean> check)
    {
        this.name = name;
        this.field1 = field1;
        this.field2 = field2;
        this.check = check;
    }

    public String getName()
    {
        return name;
    }

    public boolean evaluate()
    {
        boolean result = check.apply(field1, field2);
        logger.debug("checking field [{}] against field [{}] using check [{}] --> [{}]", field1.toString(), field2.toString(), name, result);
        return result;
    }

    public Field<T> getField1()
    {
        return field1;
    }

    public Field<U> getField2()
    {
        return field2;
    }
}
