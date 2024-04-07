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
}
