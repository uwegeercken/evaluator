package com.datamelt.evaluate.utilities;

import java.math.BigDecimal;
import java.util.Date;
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

    public String getString(String name)
    {
        return (String) fields.get(name);
    }

    public Integer getInteger(String name)
    {
        return (Integer) fields.get(name);
    }

    public Long getLong(String name)
    {
        return (Long) fields.get(name);
    }

    public Double getDouble(String name)
    {
        return (Double) fields.get(name);
    }

    public Float getFloat(String name) { return (Float) fields.get(name); }

    public Boolean getBoolean(String name)
    {
        return (Boolean) fields.get(name);
    }
    public BigDecimal getBigDecimal(String name)
    {
        return (BigDecimal) fields.get(name);
    }
    public Date getDate(String name)
    {
        return (Date) fields.get(name);
    }
}
