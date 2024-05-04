package com.datamelt.evaluate.check;

import com.datamelt.evaluate.Evaluator;
import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.model.DuplicateElementException;
import org.junit.jupiter.api.Test;

import java.util.List;
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
    public void testGroupUsingNorConditionSuccess()
    {
        // NOR only gives a true result, if all checks are false
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .connectingChecksUsing(ConnectorType.NOR)
                .addCheck("is smaller", value -> value > 1000) // false
                .addCheck("equals", value -> Objects.equals(value, 400)) // false
                .addCheck("equals", value -> Objects.equals(value, 5000)) // false
                .build();
        assert(group1.evaluateChecks(200));
    }

    @Test
    public void testGroupUsingNorConditionFailed()
    {
        // NOR only gives a true result, if all checks are false
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .connectingChecksUsing(ConnectorType.NOR)
                .addCheck("is smaller", value -> value > 100) // true
                .addCheck("equals", value -> Objects.equals(value, 400)) // false
                .addCheck("equals", value -> Objects.equals(value, 5000)) // false
                .build();
        assert(!group1.evaluateChecks(200));
    }

    @Test
    public void testGroupUsingNotConditionSuccess()
    {
        // NOT gives a true result, if at least one check is false
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .connectingChecksUsing(ConnectorType.NOT)
                .addCheck("is smaller", value -> value > 100) // true
                .addCheck("equals", value -> Objects.equals(value, 200)) // false
                .addCheck("equals", value -> Objects.equals(value, 5000)) // false
                .build();
        assert(group1.evaluateChecks(200));
    }

    @Test
    public void testGroupUsingNotConditionFailed()
    {
        // NOT gives a true result, if at least one check is false
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .connectingChecksUsing(ConnectorType.NOT)
                .addCheck("is smaller", value -> value > 100) // true
                .addCheck("equals", value -> Objects.equals(value, 200)) // false
                .addCheck("equals", value -> Objects.equals(value, 200)) // false
                .build();
        assert(!group1.evaluateChecks(200));
    }

    @Test
    public void testThreeGroupsUsingOrAndConditionSuccess()
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
    public void testThreeGroupsUsingOrAndConditionFailed()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .addCheck("is greater", value -> value > 10000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .addCheck("is greater", value -> value > 5000)
                        .build(), ConnectorType.OR)
                .addGroup(new Group.Builder<Integer>("group3")
                        .addCheck("is smaller", value -> value < 7500)
                        .build(), ConnectorType.AND)
                .build();

        assert(!Evaluator.evaluate(logic, 8000));
    }

    @Test
    public void testThreeGroupsUsingAndNorConditionSuccess()
    {
        // NOR only gives a true result, if all groups are false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .addCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .addCheck("is greater", value -> value > 10000)
                        .build(), ConnectorType.AND)
                .addGroup(new Group.Builder<Integer>("group3")
                        .addCheck("is smaller", value -> value < 7500)
                        .build(), ConnectorType.NOR)
                .build();

        List<String> results = logic.getGroup("group100").test(8000, CheckResultFilterType.ALL);
        assert(Evaluator.evaluate(logic, 8000));
    }

    @Test
    public void testThreeGroupsUsingAndNorConditionFailed()
    {
        // NOR only gives a true result, if the group and the result of the previous groups are false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .addCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .addCheck("is greater", value -> value > 5000)
                        .build(), ConnectorType.AND)
                .addGroup(new Group.Builder<Integer>("group3")
                        .addCheck("is smaller", value -> value < 7500)
                        .build(), ConnectorType.NOR)
                .build();

        assert(!Evaluator.evaluate(logic, 8000));
    }

    @Test
    public void testThreeGroupsUsingAndNotConditionSucess()
    {
        // NOT gives a true result, if at least one of the group and the result of the previous groups is false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .addCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .addCheck("is greater", value -> value > 5000)
                        .build(), ConnectorType.AND)
                .addGroup(new Group.Builder<Integer>("group3")
                        .addCheck("is smaller", value -> value < 4000)
                        .build(), ConnectorType.NOT)
                .build();

        assert(Evaluator.evaluate(logic, 8000));
    }

    @Test
    public void testThreeGroupsUsingAndNotConditionFailed()
    {
        // NOT gives a true result, if at least one of the group and the result of the previous groups is false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .addCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .addCheck("is greater", value -> value > 5000)
                        .build(), ConnectorType.AND)
                .addGroup(new Group.Builder<Integer>("group3")
                        .addCheck("is smaller", value -> value < 10000)
                        .build(), ConnectorType.NOT)
                .build();

        assert(!Evaluator.evaluate(logic, 8000));
    }

    @Test
    public void testThreeGroupsUsingAndNotConditionSuccess()
    {
        // NOT gives a true result, if at least one group is false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .addCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .addCheck("is greater", value -> value > 5000)
                        .build(), ConnectorType.AND)
                .addGroup(new Group.Builder<Integer>("group3")
                        .addCheck("is smaller", value -> value < 7500)
                        .build(), ConnectorType.NOT)
                .build();

        assert(Evaluator.evaluate(logic, 8000));
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