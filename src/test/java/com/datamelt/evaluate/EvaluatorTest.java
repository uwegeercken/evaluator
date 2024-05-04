package com.datamelt.evaluate;

import com.datamelt.evaluate.check.Group;
import com.datamelt.evaluate.check.Logic;
import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.utilities.Row;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class EvaluatorTest
{
    @Test
    public void testLogicPassedUsingSingleRowObject()
    {
        Row testRow  = new Row.Builder()
            .addField("name", "Charles")
            .addField("age", 47)
            .addField("city", "Frankfurt")
            .addField("country", "Germany")
            .build();

        Logic<Row> logic = new Logic.Builder<Row>()
                .addGroup(new Group.Builder<Row>("group1")
                        .connectingChecksUsing(ConnectorType.OR)
                        .addCheck("it's Charles", row -> row.getString("name").equals("Charles"))
                        .addCheck("it's Peter", row -> row.getString("name").equals("Peter"))
                        .build())
                .addGroup(new Group.Builder<Row>("group2")
                        .addCheck("living in Germany", row -> row.getString("country").equalsIgnoreCase("germany"))
                        .build(), ConnectorType.AND)
                .build();

        assert(Evaluator.evaluate(logic,testRow));
    }

    @Test
    public void testLogicPassedUsingMultipleRowObjects()
    {
        Row testRow1  = new Row.Builder()
                .addField("name", "Charles")
                .addField("age", 47)
                .addField("city", "Frankfurt")
                .addField("country", "Germany")
                .build();

        Row testRow2  = new Row.Builder()
                .addField("name", "Peter")
                .addField("age", 32)
                .addField("city", "Hamburg")
                .addField("country", "Germany")
                .build();

        List<Row> rows = new ArrayList<>();
        rows.add(testRow1);
        rows.add(testRow2);

        Logic<Row> logic = new Logic.Builder<Row>()
                .addGroup(new Group.Builder<Row>("group1")
                        .connectingChecksUsing(ConnectorType.OR)
                        .addCheck("name equals", row -> row.getString("name").equals("Charles"))
                        .addCheck("name equals", row -> row.getString("name").equals("Peter"))
                        .build())
                .addGroup(new Group.Builder<Row>("group2")
                        .addCheck("in Germany", row -> row.getString("country").equalsIgnoreCase("germany"))
                        .addCheck("age at or above 20", row -> row.getInteger("age") >= 20)
                        .build(), ConnectorType.AND)
                .build();

        boolean totalResult = rows.stream()
                .map(row -> Evaluator.evaluate(logic, row))
                .allMatch(evaluationResult -> evaluationResult);

        assert(totalResult);
    }

    @Test
    public void testLogicUsingStringOfValues()
    {
        String testData = "Charles;47;Frankfurt;Germany";

        Logic<String[]> logic = new Logic.Builder<String[]>()
                .addGroup(new Group.Builder<String[]>("group1")
                        .addCheck("name equals", fieldArray ->  fieldArray[0].equals("Charles"))
                        .build())
                .addGroup(new Group.Builder<String[]>("group2")
                        .addCheck("is greater", fieldArray ->  Integer.parseInt(fieldArray[1]) > 18)
                        .build(), ConnectorType.AND)
                .build();

        assert(Evaluator.evaluate(logic,testData.split(";")));
    }
}