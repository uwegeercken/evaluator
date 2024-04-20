package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class GroupTest
{
    @Test
    public void testGroupUsingAndConditionSuccessFul()
    {
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .addCheck("is smaller", value -> value > 1)
                .addCheck("equals", value -> Objects.equals(value, 200))
                .build();
        assert(group1.evaluateChecks(200));
    }

    @Test
    public void testGroupUsingAndConditionFailed()
    {
        Group<String> group1 = new Group.Builder<String>("group1")
                .addCheck("is greater", value -> value.length() < 1000)
                .addCheck("equals", value -> value.equals("hello"))
                .build();
        assert(!group1.evaluateChecks("Alibaba"));
    }

    @Test
    public void testGroupUsingOrConditionSuccessFul()
    {
        Group<Long> group1 = new Group.Builder<Long>("group1")
                .connectingChecksUsing(ConnectorType.OR)
                .addCheck("is greater", value -> value >1000)
                .addCheck("equals", value -> value!=10)
                .build();
        assert(group1.evaluateChecks(333L));

    }

    @Test
    public void testGroupUsingOrConditionFailed()
    {
        Group<Integer> group1 = new Group.Builder<Integer>("group1")
                .connectingChecksUsing(ConnectorType.OR)
                .addCheck("is greater", value -> value==0)
                .addCheck("equals", value -> value==1)
                .build();
        assert(!group1.evaluateChecks(9999));
    }
}