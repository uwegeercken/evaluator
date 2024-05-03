package com.datamelt.evaluate.check;

import com.datamelt.evaluate.Evaluator;
import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.model.DuplicateElementException;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GroupTest
{
    @Test
    public void testGroupUsingAndConditionSuccess()
    {
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .addCheck("is smaller", value -> value < 1000)
                .addCheck("equals", value -> Objects.equals(value, 200))
                .build();
        assert(group1.evaluateChecks(200));
    }

    @Test
    public void testGroupUsingAndConditionFailed()
    {
        Group<String> group1 = new Group.Builder<String>("group1")
                .addCheck("is greater", value -> value.length() > 1000)
                .addCheck("equals", value -> value.equals("hello"))
                .build();
        assert(!group1.evaluateChecks("Alibaba"));
    }

    @Test
    public void testGroupUsingOrConditionSuccess()
    {
        Group<Long> group1 = new Group.Builder<Long>("group1")
                .connectingChecksUsing(ConnectorType.OR)
                .addCheck("is greater", value -> value > 1000)
                .addCheck("equals", value -> value==333)
                .build();
        assert(group1.evaluateChecks(333L));

    }

    @Test
    public void testGroupUsingOrConditionFailed()
    {
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .connectingChecksUsing(ConnectorType.OR)
                .addCheck("is greater", value -> value > 10000)
                .addCheck("equals", value -> value == 1)
                .build();
        assert(!group1.evaluateChecks(9999));
    }

    @Test
    public void testThreeGroupsUsingOrConditionSuccess()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .addCheck("is greater", value -> value > 10000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .addCheck("is greater", value -> value > 5000)
                        .build(), ConnectorType.OR)
                .addGroup(new Group.Builder<Integer>("group3")
                        .addCheck("is greater", value -> value < 7500)
                        .build(), ConnectorType.AND)
                .build();
        assert(Evaluator.evaluate(logic, 6000));
    }

    @Test
    public void testThreeGroupsUsingOrConditionFailed()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .addCheck("is greater", value -> value > 10000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .addCheck("is greater", value -> value > 5000)
                        .build(), ConnectorType.OR)
                .addGroup(new Group.Builder<Integer>("group3")
                        .addCheck("is greater", value -> value < 7500)
                        .build(), ConnectorType.AND)
                .build();

        assert(!Evaluator.evaluate(logic, 8000));
    }

    @Test
    public void testTwoGroupsSameNameFailed()
    {
        Exception exception = assertThrows(DuplicateElementException.class, () -> {
            Logic<Integer> logic = new Logic.Builder<Integer>()
                    .addGroup(new Group.Builder<Integer>("group1")
                            .build())
                    .addGroup(new Group.Builder<Integer>("group1")
                            .build())
                    .build();
        });

        String expectedMessage = "group with same name [group1] was already added";
        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    public void testNoGroupFailed()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .build();

        assert(!Evaluator.evaluate(logic, -999));
    }

    @Test
    public void testGroupNoChecksFailed()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                    .build())
                .build();

        assert(!Evaluator.evaluate(logic, -1));
    }
}