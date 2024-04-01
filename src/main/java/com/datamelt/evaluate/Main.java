package com.datamelt.evaluate;

import java.time.LocalDate;
import java.util.Objects;

public class Main
{
    public static void main(String[] args)
    {
        LocalDate today = LocalDate.now();
        System.out.println(today.getYear());

        Field<Integer> field_01 = new Field<>("f1", 100);
        Field<String> field_03 = new Field<>("f3", "Test 1");
        Field<Integer> field_05 = new Field<>("f5", 100);
        Field<Double> field_06 = new Field<>("f5", 100.12);
        Field<Integer> field_07 = new Field<>("f5", 2024);
        Field<LocalDate> field_08 = new Field<>("f5", today );

        Check<Integer, LocalDate> testYear = new Check<>("is current year", field_07, field_08,(f1, f2) -> f1.getValue() == f2.getValue().getYear());

        Evaluator evaluator = new Evaluator.Builder()
                .addGroup(new Group.Builder("group1")
                        .addCheck(new Check<>("length smaller than", field_03, field_01,(f1, f2) -> f1.getValue().length()< f2.getValue()))
                        .addCheck(new Check<>("equals", field_01, field_05,(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                        .addCheck(testYear)
                        .build())
                .addGroup(new Group.Builder("group2")
                        .addCheck(new Check<>("length greater than", field_03, field_01,(f1, f2) -> f1.getValue().length()> f2.getValue()))
                        .addCheck(new Check<>("equals", field_01, field_05,(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                        .build())
                .build();

        boolean evaluatorResult = evaluator.evaluateGroupChecks();
        System.out.println("evaluator result for all checks and groups: " + evaluatorResult);
    }
}