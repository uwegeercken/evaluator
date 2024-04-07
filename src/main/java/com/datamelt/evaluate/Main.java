package com.datamelt.evaluate;

public class Main
{
    public static void main(String[] args)
    {
        TestProvider testProvider = new TestProvider("hello;100;200;2024");
        boolean result1 = testProvider.mapValues().evaluate();

        System.out.println("evaluator result for all checks and groups: " + result1);

    }
}