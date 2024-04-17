package com.datamelt.testimplementation;

import java.time.LocalDate;
import java.util.function.BiFunction;

public class TestProvider2
{
    private final BiFunction<Integer,LocalDate,Boolean> checkYear = (f1, f2) -> f1 == f2.getYear();

//    @Override
//    public Logic mapValues(String data)
//    {
//        String[] values = data.split(";");
//
//        return new Logic.Builder()
//                .addGroup(new Group.Builder("group1")
//                        .connectingChecksUsing(ConnectorType.OR)
//                        .addCheck(new Check<>("length smaller than", values[0], Integer.parseInt(values[1]),(f1, f2) -> f1.length() < f2))
//                        .addCheck(new Check<>("equals", values[1], Integer.parseInt(values[2]),(f1, f2) -> Objects.equals(f1, f2)))
//                        .addCheck(new Check<>("checkYear", Integer.parseInt(values[3]), LocalDate.now(), checkYear))
//                        .build())
//                .addGroup(new Group.Builder("group2")
//                        .addCheck(new Check<>("equals", 1, 1,(f1, f2) -> Objects.equals(f1, f2)))
//                        .build())
//                .build();
//    }
}
