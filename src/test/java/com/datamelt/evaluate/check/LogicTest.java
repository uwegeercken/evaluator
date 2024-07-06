package com.datamelt.evaluate.check;

import com.datamelt.evaluate.sample.Person;
import com.datamelt.evaluate.utilities.Row;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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
        String[] testDataFields = testData.split(";");

        Logic<String[]> logic = new Logic.Builder<String[]>()
                .addGroup(new Group.Builder<String[]>("group1")
                        .withCheck("name equals", fields ->  fields[0].equals("Charles"))
                        .build())
                .addGroup(new Group.Builder<String[]>("group2")
                        .withCheck("is greater", fields ->  Integer.parseInt(fields[1]) > 18)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .build();

        assert(logic.evaluate(testDataFields).passed());
    }

    @Test
    public void testLogicUsingMap()
    {
        Map<String,String> testData = new HashMap<>();
        testData.put("name", "Charles");
        testData.put("age","47");

        Logic<Map<String,String>> logic = new Logic.Builder<Map<String,String>>()
                .addGroup(new Group.Builder<Map<String,String>>("group1")
                        .withCheck("name equals", map ->  map.get("name").equals("Charles"))
                        .build())
                .addGroup(new Group.Builder<Map<String,String>>("group2")
                        .withCheck("is greater", map ->  Integer.parseInt(map.get("age")) > 40)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .build();

        assert(logic.evaluate(testData).passed());
    }

    @Test
    public void testLogicUsingMapAndPredicate()
    {
        Map<String,String> testData = new HashMap<>();
        testData.put("name", "Charles");
        testData.put("age","47");

        Predicate<Map<String,String>> isCharles = map -> map.get("name").equals("Charles");
        Predicate<Map<String,String>> ageAbove40 = map -> Integer.parseInt(map.get("age")) > 40;

        Logic<Map<String,String>> logic = new Logic.Builder<Map<String,String>>()
                .addGroup(new Group.Builder<Map<String,String>>("group1")
                        .withCheck("name equals", isCharles)
                        .build())
                .addGroup(new Group.Builder<Map<String,String>>("group2")
                        .withCheck("is greater", ageAbove40)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .build();

        assert(logic.evaluate(testData).passed());
    }

    @Test
    public void testLogicUsingPerson()
    {
        Person person1 = new Person("Jackson", "Peter", 34);

        Predicate<Person> isPeter = person -> person.firstname().equals("Peter");
        Predicate<Person> thirtyOrOlder = person -> person.age() >= 30;


        Logic<Person> logic = new Logic.Builder<Person>()
                .addGroup(new Group.Builder<Person>("group1")
                        .withCheck("peter", isPeter)
                        .withCheck("30 or older", thirtyOrOlder)
                        .build())
                .build();

        assert(logic.evaluate(person1).passed());
    }
}