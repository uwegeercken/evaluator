package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.utilities.Field;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class GroupTest
{
    @Test
    public void testGroupUsingAndConditionSuccessFul()
    {
        Group group1 = new Group.Builder("group1")
                .addCheck(new Check<>("is smaller", new Field<>("field-01",100), new Field<>("field-02",200),(f1, f2) -> f1.getValue() < f2.getValue()))
                .addCheck(new Check<>("equals", new Field<>("field-03",200), new Field<>("field-03",200),(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                .build();
        assert(group1.evaluateChecks());
    }

    @Test
    public void testGroupUsingAndConditionFailed()
    {
        Group group1 = new Group.Builder("group1")
                .addCheck(new Check<>("is greater", new Field<>("field-01",100), new Field<>("field-02",200),(f1, f2) -> f1.getValue() > f2.getValue()))
                .addCheck(new Check<>("equals", new Field<>("field-03",200), new Field<>("field-03",200),(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                .build();
        assert(!group1.evaluateChecks());
    }

    @Test
    public void testGroupUsingOrConditionSuccessFul()
    {
        Group group1 = new Group.Builder("group1")
                .connectingChecksUsing(ConnectorType.OR)
                .addCheck(new Check<>("is greater", new Field<>("field-01",100), new Field<>("field-02",200),(f1, f2) -> f1.getValue() > f2.getValue()))
                .addCheck(new Check<>("equals", new Field<>("field-03",200), new Field<>("field-03",200),(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                .build();
        assert(group1.evaluateChecks());

    }

    @Test
    public void testGroupUsingOrConditionFailed()
    {
        Group group1 = new Group.Builder("group1")
                .connectingChecksUsing(ConnectorType.OR)
                .addCheck(new Check<>("is greater", new Field<>("field-01",100), new Field<>("field-02",200),(f1, f2) -> f1.getValue() > f2.getValue()))
                .addCheck(new Check<>("equals", new Field<>("field-03",200), new Field<>("field-03",300),(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                .build();
        assert(!group1.evaluateChecks());
    }

    @Test
    public void testMixedTypeGroupUsingAndConditionSuccessFul()
    {
        Group group1 = new Group.Builder("group1")
                .addCheck(new Check<>("length equals", new Field<>("field-01","1234567890"), new Field<>("field-02",10),(f1, f2) -> f1.getValue().length() == f2.getValue()))
                .addCheck(new Check<>("equals", new Field<>("field-03",200), new Field<>("field-03",200),(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                .build();

        assert(group1.evaluateChecks());
    }
}