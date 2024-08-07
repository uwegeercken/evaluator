package com.datamelt.evaluate.check;

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
                .withCheck("is smaller", value -> value < 1000)
                .withCheck("equals", value -> Objects.equals(value, 200))
                .build();
        GroupResult<Integer> groupResult = new GroupResult<>(group1,200);

        assert(new GroupResult<>(group1,200).passed());
    }

    @Test
    public void testGroupUsingAndConditionFailed()
    {
        Group<String> group1 = new Group.Builder<String>("group1")
                .withCheck("is greater", value -> value.length() > 1000)
                .withCheck("equals", value -> value.equals("hello"))
                .build();
        assert(!new GroupResult<>(group1,"Alibaba").passed());
    }

    @Test
    public void testGroupUsingOrConditionSuccess()
    {
        Group<Long> group1 = new Group.Builder<Long>("group1")
                .connectorBetweenChecks(CheckConnectorType.OR)
                .withCheck("is greater", value -> value > 1000)
                .withCheck("equals", value -> value==333)
                .build();
        assert(new GroupResult<>(group1,333L).passed());

    }

    @Test
    public void testGroupUsingOrConditionFailed()
    {
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .connectorBetweenChecks(CheckConnectorType.OR)
                .withCheck("is greater", value -> value > 10000)
                .withCheck("equals", value -> value == 1)
                .build();
        assert(!new GroupResult<>(group1,9999).passed());
    }

    @Test
    public void testThreeGroupsUsingOrAndConditionSuccess()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .withCheck("is greater", value -> value > 10000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .withCheck("is greater", value -> value > 5000)
                        .connectorToPreviousGroup(GroupConnectorType.OR)
                        .build())
                .addGroup(new Group.Builder<Integer>("group3")
                        .withCheck("is greater", value -> value < 7500)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .build();

        EvaluationResult<Integer> result = logic.evaluate(6000);
        assert(logic.evaluate(6000).passed());
    }

    @Test
    public void testThreeGroupsUsingOrAndConditionFailed()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .withCheck("is greater", value -> value > 10000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .withCheck("is greater", value -> value > 5000)
                        .connectorToPreviousGroup(GroupConnectorType.OR)
                        .build())
                .addGroup(new Group.Builder<Integer>("group3")
                        .withCheck("is smaller", value -> value < 7500)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .build();

        assert(!logic.evaluate(8000).passed());
    }

    @Test
    public void testThreeGroupsUsingAndNorConditionSuccess()
    {
        // NOR only gives a true result, if all groups are false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .withCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .withCheck("is greater", value -> value > 10000)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .addGroup(new Group.Builder<Integer>("group3")
                        .withCheck("is smaller", value -> value < 7500)
                        .connectorToPreviousGroup(GroupConnectorType.OR_NOT)
                        .build())
                .build();

        assert(logic.evaluate(8000).passed());
    }

    @Test
    public void testThreeGroupsUsingAndNorConditionFailed()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .withCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .withCheck("is smaller equals", value -> value <= 5000)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .addGroup(new Group.Builder<Integer>("group3")
                        .withCheck("is greater", value -> value > 10000)
                        .connectorToPreviousGroup(GroupConnectorType.OR_NOT)
                        .build())
                .build();

        assert(logic.evaluate(15000).failed());
    }

    @Test
    public void testThreeGroupsUsingAndNotConditionSucess()
    {
        // NOT gives a true result, if at least one of the group and the result of the previous groups is false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .withCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .withCheck("is greater", value -> value > 5000)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .addGroup(new Group.Builder<Integer>("group3")
                        .withCheck("is smaller", value -> value < 4000)
                        .connectorToPreviousGroup(GroupConnectorType.AND_NOT)
                        .build())
                .build();

        assert(logic.evaluate(8000).passed());
    }

    @Test
    public void testThreeGroupsUsingAndNotConditionFailed()
    {
        // NOT gives a true result, if at least one of the group and the result of the previous groups is false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .withCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .withCheck("is greater", value -> value > 5000)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .addGroup(new Group.Builder<Integer>("group3")
                        .withCheck("is smaller", value -> value < 10000)
                        .connectorToPreviousGroup(GroupConnectorType.AND_NOT)
                        .build())
                .build();

        assert(!logic.evaluate(8000).passed());
    }

    @Test
    public void testThreeGroupsUsingAndNotConditionSuccess()
    {
        // NOT gives a true result, if at least one group is false
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                        .withCheck("is greater", value -> value > 1000)
                        .build())
                .addGroup(new Group.Builder<Integer>("group2")
                        .withCheck("is greater", value -> value > 5000)
                        .connectorToPreviousGroup(GroupConnectorType.AND)
                        .build())
                .addGroup(new Group.Builder<Integer>("group3")
                        .withCheck("is smaller", value -> value < 7500)
                        .connectorToPreviousGroup(GroupConnectorType.AND_NOT)
                        .build())
                .build();

        assert(logic.evaluate(8000).passed());
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

        assert(!logic.evaluate(-999).passed());
    }

    @Test
    public void testGroupNoChecksFailed()
    {
        Logic<Integer> logic = new Logic.Builder<Integer>()
                .addGroup(new Group.Builder<Integer>("group1")
                    .build())
                .build();

        assert(!logic.evaluate(-1).passed());
    }
}