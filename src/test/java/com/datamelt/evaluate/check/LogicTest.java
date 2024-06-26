package com.datamelt.evaluate.check;

import com.datamelt.evaluate.utilities.Row;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class LogicTest
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
                        .connectorBetweenChecks(CheckConnectorType.OR)
                        .withCheck("it's Charles", row -> row.getString("name").equals("Charles"))
                        .withCheck("it's Peter", row -> row.getString("name").equals("Peter"))
                        .build())
                .addGroup(new Group.Builder<Row>("group2")
                        .withCheck("living in Germany", row -> row.getString("country").equalsIgnoreCase("germany"))
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .build();

        EvaluationResult<Row> evaluationResult = logic.evaluate(testRow);
        List<CheckResult> checkResults = evaluationResult.getCheckResults("group1", CheckResultFilterType.PASSED_ONLY);
        assert(evaluationResult.passed());
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
                        .connectorBetweenChecks(CheckConnectorType.OR)
                        .withCheck("name equals", row -> row.getString("name").equals("Charles"))
                        .withCheck("name equals", row -> row.getString("name").equals("Peter"))
                        .build())
                .addGroup(new Group.Builder<Row>("group2")
                        .withCheck("in Germany", row -> row.getString("country").equalsIgnoreCase("germany"))
                        .withCheck("age at or above 20", row -> row.getInteger("age") >= 20)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .build();

        boolean totalResult = rows.stream()
                .map(logic::evaluate)
                .allMatch(EvaluationResult::passed);

        assert(totalResult);
    }

    @Test
    public void testLogicUsingStringOfValues()
    {
        String testData = "Charles;47;Frankfurt;Germany";

        Logic<String[]> logic = new Logic.Builder<String[]>()
                .addGroup(new Group.Builder<String[]>("group1")
                        .withCheck("name equals", fieldArray ->  fieldArray[0].equals("Charles"))
                        .build())
                .addGroup(new Group.Builder<String[]>("group2")
                        .withCheck("is greater", fieldArray ->  Integer.parseInt(fieldArray[1]) > 18)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .build();

        assert(logic.evaluate(testData.split(";")).passed());
    }
}