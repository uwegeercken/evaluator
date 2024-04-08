package com.datamelt.evaluate.utilities;

import java.util.HashMap;
import java.util.Map;

public class Row
{
    private final Map<String, Object> fields = new HashMap<>();
    public void addField(String name, Object value)
    {
        fields.put(name, value);
    }

    public Map<String, Object> getFields()
    {
        return fields;
    }

    public String getStringValue(String name)
    {
        return (String) fields.get(name);
    }

    public Integer getIntegerValue(String name)
    {
        return (Integer) fields.get(name);
    }

    public Long getLongValue(String name)
    {
        return (Long) fields.get(name);
    }

    public Double getDoubleValue(String name)
    {
        return (Double) fields.get(name);
    }

    public Float getFloatValue(String name)
    {
        return (Float) fields.get(name);
    }

    public Boolean getBooleanValue(String name)
    {
        return (Boolean) fields.get(name);
    }
}
