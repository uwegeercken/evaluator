# evaluator

Library to construct logic to check values using flexible AND and OR conditions and grouping.

Checks can be added to groups, where the checks are connected using an AND or an OR condition. Multiple groups can
also be connected using an AND or an OR condition. This way one can create complex logic easily.

The default connector between the checks of a group as well as for the connector between groups is AND. Note that the connector
between groups defines the connector type to the previous group. So if you have three groups where group2 is connected to group1 with
an OR connector and group3 is connected to group2 with an AND connector, the following logic will be applied: 

    ((result group1 OR result group2) AND result group3)

So the result of group3 will be combined with the combined result of group1 and group2. You can retrieve a description of the connection logic using
the logic class method getGroupConnectionLogic().

Checks have a name plus the logic to apply to the data. The logic is a lambda expression which
evaluates to a boolean true or false.

Below is a String named testData representing multiple fields, all separated by a semicolon. Define the logic consisting of a group and it's
checks like this:

    // testData fields: Name,Age,City,Country, Shoesize
    String testData = "Charles;47;Frankfurt;Germany,48";

    Logic<String[]> logic = new Logic.Builder<String[]>()
                .addGroup(new Group.Builder<String[]>("group1")
                        .addCheck("name equals", fieldArray ->  fieldArray[0].equals("Charles"))
                        .build())
                .build();

The logic and the group expect a string array as the type parameter. To test if the test data passes all checks (rules) simply call:

    boolean result = Evaluator.evaluate(logic, testData.split(";"))

You may use multiple groups. When adding a group to the logic you can specify the connection type (AND or OR) to the previous group. When adding
checks to a group you can specify how the checks within the group are connected (AND or OR) using the connectingChecksUsing(...) method.

    Logic<String[]> logic = new Logic.Builder<String[]>()
                .addGroup(new Group.Builder<String[]>("group1")
                    .connectingChecksUsing(ConnectorType.OR)
                    .addCheck("it's Charles", fieldArray ->  fieldArray[0].equals("Charles"))
                    .addCheck("it's Peter", fieldArray ->  fieldArray[0].equals("Peter"))
                    .build())
                .addGroup(new Group.Builder<String[]>("group2")
                    .addCheck("is grown up", fieldArray ->  Integer.parseInt(fieldArray[1]) > 18)
                    .build(), ConnectorType.AND)
                .addGroup(new Group.Builder<String[]>("group2")
                    .addCheck("with big shoesize", fieldArray ->  Integer.parseInt(fieldArray[4]) > 44)
                    .build(), ConnectorType.OR)
            .build();

Instead of using a string array like in the case above, you can use any other object that you want to test against a defined logic. Like
e.g. a record from a SQL resultset or a row that contains all fields parsed from a CSV file. Using an object that represents a row of attributes
or fields will allow you to compare fields against each other. Or you may check a field against a defined value.

You may use the getGroupConnectionLogic() method to retrieve a string representation of the logic and how groups and checks are evaluated. 

Instead of defining lambda expressions for the checks over and over again, you could also put them in a different class or library and use them as
static variables.

Using groups, the "and" or "or" connector between the individual checks, as well as the "and" or "or" between the different groups
you can build very complex logic in a simple way without the need to use lots of brackets.

last update: Uwe Geercken - 2024/05/01