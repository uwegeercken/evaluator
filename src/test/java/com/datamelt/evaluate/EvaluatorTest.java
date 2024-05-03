package com.datamelt.evaluate;

import com.datamelt.evaluate.check.Group;
import com.datamelt.evaluate.check.Logic;
import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.utilities.Row;
import org.junit.jupiter.api.Test;

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
                        .addCheck("name equals", row -> row.getString("name").equals("Charles"))
                        .addCheck("city equals", row -> row.getString("city").equals("Frankfurt"))
                        .addCheck("country equals", row -> row.getString("country").equals("Germany"))
                        .build())
                .addGroup(new Group.Builder<Row>("group2")
                        .addCheck("age is greater", row -> row.getInteger("age") > 18)
                        .build(), ConnectorType.AND)
                .build();

        assert(Evaluator.evaluate(logic,testRow));
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