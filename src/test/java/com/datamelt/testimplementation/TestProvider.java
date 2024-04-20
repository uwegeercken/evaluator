package com.datamelt.testimplementation;

import com.datamelt.evaluate.Evaluator;
import com.datamelt.evaluate.check.Group;
import com.datamelt.evaluate.check.Logic;
import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.utilities.Row;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiFunction;

public class TestProvider
{
    private final BiFunction<Integer,LocalDate,Boolean> checkYear = (f1, f2) -> f1 == f2.getYear();

    public static void main(String[] args)
    {
        Logic<Row> logic = new Logic.Builder<Row>()
                .addGroup(new Group.Builder<Row>("group1")
                        .addCheck("length smaller than", f1 -> Objects.equals(f1.getString("test1"), "gaga"))
                        .build())
                .addGroup(new Group.Builder<Row>("group2")
                        .addCheck("length greater than", f1 -> Objects.equals(f1.getString("test1"), "bubu"))
                        .build(),ConnectorType.AND)
                .build();

        Logic<String> logic2 = new Logic.Builder<String>()
                .addGroup(new Group.Builder<String>("group1")
                        .addCheck("length smaller than", f1 -> f1.length() == 4)
                        .build(), ConnectorType.OR)
                .build();

        Row row = new Row();
        row.addField("test1","gaga");

        boolean result = Evaluator.evaluate(logic, row);

        boolean result2 = Evaluator.evaluate(logic2,"abcd");
    }


}
