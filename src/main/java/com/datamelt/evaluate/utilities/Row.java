package com.datamelt.evaluate.utilities;

import com.datamelt.evaluate.model.InvalidFieldReferenceException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Row
{
    private final Map<String, Object> fields;

    private Row(Builder builder)
    {
        this.fields = builder.fields;
    }

    public Map<String, Object> getFields()
    {
        return fields;
    }

    public String getString(String name) throws InvalidFieldReferenceException
    {
        if(fields.containsKey(name))
        {
            return (String) fields.get(name);
        }
        else
        {
            throw new InvalidFieldReferenceException("undefined field with name [" + name + "]");
        }
    }

    public Integer getInteger(String name) throws InvalidFieldReferenceException
    {
        if(fields.containsKey(name))
        {
            return (Integer) fields.get(name);
        }
        else
        {
            throw new InvalidFieldReferenceException("undefined field with name [" + name + "]");
        }
    }

    public Long getLong(String name) throws InvalidFieldReferenceException
    {
        if(fields.containsKey(name))
        {
            return (Long) fields.get(name);
        }
        else
        {
            throw new InvalidFieldReferenceException("undefined field with name [" + name + "]");
        }
    }

    public Double getDouble(String name) throws InvalidFieldReferenceException
    {
        if(fields.containsKey(name))
        {
            return (Double) fields.get(name);
        }
        else
        {
            throw new InvalidFieldReferenceException("undefined field with name [" + name + "]");
        }
    }

    public Float getFloat(String name) throws InvalidFieldReferenceException
    {
        if(fields.containsKey(name))
        {
            return (Float) fields.get(name);
        }
        else
        {
            throw new InvalidFieldReferenceException("undefined field with name [" + name + "]");
        }
    }

    public Boolean getBoolean(String name) throws InvalidFieldReferenceException
    {
        if(fields.containsKey(name))
        {
            return (Boolean) fields.get(name);
        }
        else
        {
            throw new InvalidFieldReferenceException("undefined field with name [" + name + "]");
        }
    }
    public BigDecimal getBigDecimal(String name) throws InvalidFieldReferenceException
    {
        if(fields.containsKey(name))
        {
            return (BigDecimal) fields.get(name);
        }
        else
        {
            throw new InvalidFieldReferenceException("undefined field with name [" + name + "]");
        }
    }
    public Date getDate(String name) throws InvalidFieldReferenceException
    {
        if(fields.containsKey(name))
        {
            return (Date) fields.get(name);
        }
        else
        {
            throw new InvalidFieldReferenceException("undefined field with name [" + name + "]");
        }
    }

    public static class Builder
    {
        private final Map<String, Object> fields = new HashMap<>();

        public Builder addField(String name, Object value)
        {
            fields.put(name, value);
            return this;
        }

        public Row build()
        {
            return new Row(this);
        }
    }
}
