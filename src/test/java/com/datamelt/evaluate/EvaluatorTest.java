package com.datamelt.evaluate;

class EvaluatorTest
{
//    @Test
//    public void testLogicUsingRowObject()
//    {
//        Row row  = new Row();
//        row.addField("field-0", "hello");
//        row.addField("field-1", 100);
//        row.addField("field-2", 200);
//        row.addField("field-3", 2024);
//
//        LogicProvider<Row> logicProvider1 = new TestProvider();
//
//        assert(Evaluator.evaluate(logicProvider1, row));
//    }
//
//    @Test
//    public void testLogicUsingMultipleRowObjects()
//    {
//        Row row  = new Row();
//        row.addField("field-0", "hello");
//        row.addField("field-1", 100);
//        row.addField("field-2", 200);
//        row.addField("field-3", 2024);
//
//        Row row2  = new Row();
//        row2.addField("field-0", "hello");
//        row2.addField("field-1", 100);
//        row2.addField("field-2", 200);
//        row2.addField("field-3", 2024);
//
//        List<Row> rows = new ArrayList<>();
//        rows.add(row);
//        rows.add(row2);
//
//        LogicProvider<Row> logicProvider = new TestProvider();
//
//        boolean result = rows.stream()
//                .map(row1 -> Evaluator.evaluate(logicProvider, row))
//                .reduce((aBoolean, aBoolean2) -> aBoolean && aBoolean2).orElse(false);
//
//        assert(result);
//    }
//
//    @Test
//    public void testLogicUsingStringOfValues()
//    {
//        String testData = "hello;100;200;2024";
//        LogicProvider<String> logicProvider = new TestProvider2();
//        assert(Evaluator.evaluate(logicProvider, testData));
//    }
}