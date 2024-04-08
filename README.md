# logic

Library to construct logic to check values using flexible AND and OR conditions and grouping.

Checks can be added to groups, where the checks are connected using an AND or an OR condition. Multiple groups can
also be connected using an AND or an OR condition. This way one can create complex logic easily.

The default for the connector between the checks of a group as well as the connector between multiple groups is AND.

Checks have a name and specify two fields plus the logic to apply to the fields. The logic is a lambda expression which
evaluates to a boolean true or false.

Define the checks in a class which implements the LogicProvider interface, like this:

    LogicProvider<Row> logicProvider = new TestProvider();

"Row" in this sample is a class which implements a data row and its fields. The TestProvider class implements the
LogicProvider Interface and the method "mapValues(...)". In this method the logic that is to be applied on the data
is defined as well as the data values to be used.

    @Override
    public Logic mapValues(Row row)
    {
        return new Logic.Builder()
            .addGroup(new Group.Builder("group1")
                .connectingChecksUsing(ConnectorType.OR)
                .addCheck(new Check<>("length smaller than", row.getStringValue("field-0"), row.getIntegerValue("field-1"),(f1, f2) -> f1.length() < f2))
                .addCheck(new Check<>("equals", row.getIntegerValue("field-1"),row.getIntegerValue("field-2"),(f1, f2) -> Objects.equals(f1, f2)))
            .build())
            .addGroup(new Group.Builder("group2")
                .addCheck(new Check<>("equals", 1, 1,(f1, f2) -> Objects.equals(f1, f2)))
            .build())
        .build();
    }

Once the "mapValues(...)" method is defined, evaluate the defined logic/checks against the data:

        boolean result = Evaluator.evaluate(logicProvider, row));

You can - instead of a Row object shown above - e.g. use a line from a CSV file, split it into its fields and run the
logic against these fields. You may also loop over e.g. all lines in a CSV file or records of a resultset from a database
query and check the validity of the data using a defined logic.

Instead of defining lambda expressions over and over again, you could also put them in a different class or library and use them as
static variables.

Using groups, the "and" or "or" connector between the individual checks, as well as the "and" or "or" between the different groups
you can build very complex logic in a simple way without the need to use lots of brackets.

last update: Uwe Geercken - 2024/04/08