package com.datamelt.evaluate;

import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.utilities.Row;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiFunction;

public class TestProvider implements LogicProvider
{
    private final Row row = new Row();

    private final BiFunction<Integer,LocalDate,Boolean> checkYear = (f1, f2) -> f1 == f2.getYear();

    public TestProvider(String rowOfData)
    {
        // SAMPLE DATA: "hello;100;200;2024"

        String[] parts = rowOfData.split(";");
        row.addField("field-0", parts[0]);
        row.addField("field-1", Integer.valueOf(parts[1]));
        row.addField("field-2", Integer.valueOf(parts[2]));
        row.addField("field-3", Integer.valueOf(parts[3]));
    }

    @Override
    public Logic mapValues()
    {
        Logic logic = new Logic.Builder()
                .addGroup(new Group.Builder("group1")
                        .connectingChecksUsing(ConnectorType.OR)
                        .addCheck(new Check<>("length smaller than", row.getStringValue("field-0"), row.getIntegerValue("field-1"),(f1, f2) -> f1.length() < f2))
                        .addCheck(new Check<>("equals", row.getIntegerValue("field-1"),row.getIntegerValue("field-2"),(f1, f2) -> Objects.equals(f1, f2)))
                        .addCheck(new Check<>("checkYear", row.getIntegerValue("field-3"), LocalDate.now(), checkYear))
                        .build())
                .addGroup(new Group.Builder("group2")
                        .addCheck(new Check<>("equals", 1, 1,(f1, f2) -> Objects.equals(f1, f2)))
                        .build())
                .build();
        return logic;
    }
}
