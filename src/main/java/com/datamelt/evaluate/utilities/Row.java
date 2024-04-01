package com.datamelt.evaluate.utilities;



import com.datamelt.evaluate.Field;

import java.util.HashMap;
import java.util.Map;

public class Row
{
    private final Map<String, Field<?>> fields = new HashMap<>();
    public <T> void addField(Field<T> field)
    {
        fields.put(field.getName(), field);
    }

    public Map<String, Field<?>> getFields()
    {
        return fields;
    }

    public Field<Integer> getIntegerField(String name)
    {
        Field<?> field = fields.get(name);
        if(field.getValue() instanceof Integer)
        {
            return (Field<Integer>) fields.get(name);
        }
        else
        {
            throw new ClassCastException(String.format( "the value of field [%s] is not an integer", name));
        }
    }

    public Field<String> getStringField(String name)
    {
        Field<?> field = fields.get(name);
        if(field.getValue() instanceof String)
        {
            return (Field<String>) fields.get(name);
        }
        else
        {
            throw new ClassCastException(String.format( "the value of field [%s] is not a string", name));
        }
    }
}
