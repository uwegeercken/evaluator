package com.datamelt.evaluate.utilities;

import com.datamelt.evaluate.model.GenericField;

public class Field<T> implements GenericField<T>
{
    private final String name;
    private final T value;

    public Field(String name, T value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public T getValue()
    {
        return value;
    }

    public String toString()
    {
        return name + " --> " + value;
    }

}
