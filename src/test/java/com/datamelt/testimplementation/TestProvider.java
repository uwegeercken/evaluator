package com.datamelt.testimplementation;

import com.datamelt.evaluate.check.Check;
import com.datamelt.evaluate.check.Group;
import com.datamelt.evaluate.check.Logic;
import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.model.LogicProvider;
import com.datamelt.evaluate.utilities.Row;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiFunction;

public class TestProvider implements LogicProvider<Row>
{
    private final BiFunction<Integer,LocalDate,Boolean> checkYear = (f1, f2) -> f1 == f2.getYear();

    @Override
    public Logic mapValues(Row row)
    {
        return new Logic.Builder()
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
    }
}
