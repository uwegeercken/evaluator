# evaluator

Library to construct logic to check values using flexible conditions and grouping using AND, OR, NOT or NOR.

Checks can be added to groups, where the checks are connected using also AND, OR, NOT, NOR. Multiple groups can
also be connected using AND, OR, NOT, NOR. This way one can create complex logic easily.

Groups and their checks are like a unit of tests or filters using the specified connection logic between the checks. And
by connecting groups to each other the single group logic is combined to a more complex structure.

The default connector between the checks of a group as well as for the connector between groups is AND. Note that the connector
between groups defines the connector type to the previous group. So if you have three groups where group2 is connected to group1 with
an OR connector and group3 is connected to group2 with an AND connector, the following logic will be applied: 

    ((result group1 OR result group2) AND result group3)

So the result of group3 will be combined with the combined result of group1 and group2. You can retrieve a description of the connection logic using
the logic class method getGroupConnectionLogic().

If you connect multiple checks with an AND condition, then all must evaluate to "true" so that the overall result is "true".
If you connect multiple checks with an OR condition, then at least one must evaluate to "true" so that the overall result is "true".
If you connect multiple checks with an NOR condition, then all must evaluate to "false" so that the overall result is "true".
If you connect multiple checks with an NOT condition, then at least one must evaluate to "false" so that the overall result is "true".

For the groups it is the same principle, but remember that a group is connected to the result of the previous group(s) - as explained above. 

Checks have a name plus the logic to apply to the data. The logic is a lambda expression which evaluates to a boolean true or false (a predicate).

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

The static "evaluate" method returns true if the provided data passes the defined logic and returns false if not. There is also a method
available to evaluate a single group.

You can process a list of data objects easily by repeatedly calling

You may use multiple groups. When adding a group to the logic you can specify the connection type (AND, OR, NOT, NOR) to the previous group - default is AND. When adding
checks to a group you can specify how the checks within the group are connected (AND, OR, NOT, NOR) using the connectingChecksUsing(...) method - default is AND.

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

If you don't define any checks in a group the result will evaluate to "false". If you don't add any groups to a logic object then the result will evaluate also to "false".

Instead of using a string array like in the case above, you can use any other object that you want to test against a defined logic. Like
e.g. a record from a SQL resultset or a row that contains all fields parsed from a CSV file. Using an object that represents a row of attributes
or fields will allow you to compare fields against each other. Or you may check a field against a defined value.

You may use the getGroupConnectionLogic() method to retrieve a string representation of the logic and how groups and checks are evaluated. 

Instead of defining lambda expressions for the checks over and over again, you could also put them in a different class or library and use them as
static variables.

Using groups, the AND, OR, NOT, NOR as the connector between the individual checks, as well as the AND, OR, NOT, NOR between the different groups
you can build very complex logic in a simple way without the need to use lots of brackets or complicated designs with if statements.

last update: Uwe Geercken - 2024/05/04