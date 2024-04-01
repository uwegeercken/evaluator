package com.datamelt;

import com.datamelt.evaluate.Check;
import com.datamelt.evaluate.Evaluator;
import com.datamelt.evaluate.Field;
import com.datamelt.evaluate.Group;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class EvaluatorTest
{
    @Test
    public void testGroupUsingAndConditionSuccessFul()
    {
        Field<Integer> field_01 = new Field<>("f1", 100);
        Field<Integer> field_02 = new Field<>("f2", 6);
        Field<String> field_03 = new Field<>("f3", "Test 1");
        Field<Integer> field_05 = new Field<>("f5", 100);

        Evaluator evaluator = new Evaluator.Builder()
                .addGroup(new Group.Builder("group1")
                        .addCheck(new Check<>("length smaller than", field_03,field_01,(f1, f2) -> f1.getValue().length()< f2.getValue()))
                        .addCheck(new Check<>("equals", field_01,field_05,(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                        .build())
                .addGroup(new Group.Builder("group2")
                        .addCheck(new Check<>("length greater than", field_03,field_02,(f1, f2) -> f1.getValue().length()== f2.getValue()))
                        .addCheck(new Check<>("equals", field_01,field_05,(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                        .build())
                .build();

        assert(evaluator.evaluateGroupChecks());
    }
}