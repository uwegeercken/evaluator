# evaluator

### General
Library to construct validation logic using flexible conditions and grouping using AND, OR, NOT or NOR. It is easy to
implement validation logic with a few simple statements. But if the logic gets more complicated a structured approach will give more
flexibility, more readability and easier maintenance of the code.

Checks can be added to groups, where the checks are connected using AND or OR. Multiple groups can be connected to each other
by using AND, OR, AND_NOT or OR_NOT. This way one can create complex logic easily.

Groups and their checks are like a test units or filters using the specified connection logic between the checks. And
by connecting groups to each other the single group logic is combined to a more complex structure across multiple groups.

The default connector between the checks of a group as well as for the connector between groups is AND. Note that the connector
between groups defines the connector type to the **previous** group. So if you have three groups where group2 is connected to group1 with
an OR connector and group3 is connected to group2 with an AND connector, the following logic will be applied: 

    ((result group1 OR result group2) AND result group3)

So the result of group3 will be combined with the combined result of group1 and group2. You can retrieve a description of the connection logic using
the getGroupConnectionLogic() method in the logic class.

If you connect multiple groups with an AND condition, then all must evaluate to "true" so that the overall result is "true".
If you connect multiple groups with an OR condition, then at least one must evaluate to "true" so that the overall result is "true".
If you connect multiple groups with an OR_NOT condition, then either the first group ia true or the second group is not true.
If you connect multiple groups with an AND_NOT condition, then the first one must evaluate to "true" and the other one to "false" so that the overall result is "true".

Checks have a name plus the logic to apply to the data. The logic is a lambda expression which evaluates to a boolean true or false (a predicate).

Below is a String named testData representing multiple fields, all separated by a semicolon. Define the logic consisting of a group and it's
checks like this:

    // testData fields: Name,Age,City,Country,Shoesize
    String testData = "Charles;47;Frankfurt;Germany,48";

    Logic<String[]> logic = new Logic.Builder<String[]>()
                .addGroup(new Group.Builder<String[]>("group1")
                        .withCheck("name equals", fieldArray ->  fieldArray[0].equals("Charles"))
                        .build())
                .build();

The logic object and the group object expects - in this example - a string array as the type parameter. To evaluate if the test data passes all checks (rules) simply call:

    EvaluationResult<String[]> result = logic.evaluate(testData.split(";"))

The testData String is split into its fields. The static "evaluate" method returns an EvaluationResult. It contains the results of all checks and groups. The passed method returns true if the provided data passes the defined logic and returns false if not.
You can process a list of data objects easily by repeatedly calling the logic.evaluate(<data object>) method.

#### Multiple Groups
You may use multiple groups. When adding a group to the logic you can specify the connection type (AND, OR, NOT, NOR) to the previous group using the connectorToPreviousGroup method - default is AND. When adding
checks to a group you can specify how the checks within the group are connected (AND, OR, NOT, NOR) using the connectorBetweenChecks(...) method - default is AND.

    Logic<String[]> logic = new Logic.Builder<String[]>()
                .addGroup(new Group.Builder<String[]>("group1")
                    .connectorBetweenChecks(ConnectorType.OR)
                    .withCheck("it's Charles", fieldArray ->  fieldArray[0].equals("Charles"))
                    .withCheck("it's Peter", fieldArray ->  fieldArray[0].equals("Peter"))
                    .build())
                .addGroup(new Group.Builder<String[]>("group2")
                    .withCheck("is grown up", fieldArray ->  Integer.parseInt(fieldArray[1]) > 18)
                    .connectorToPreviousGroup(ConnectorType.AND)
                    .build())
                .addGroup(new Group.Builder<String[]>("group3")
                    .withCheck("with big shoesize", fieldArray ->  Integer.parseInt(fieldArray[4]) > 44)
                    .connectorToPreviousGroup(ConnectorType.OR)
                    .build())
            .build();

If you don't define any checks in a group the result will evaluate to "false". If you don't add any groups to a logic object then the result will evaluate also to "false".

#### Looping with Java streams
If you have a list of objects, you can loop and evaluate if the data passes all checks of the logic. Here - as an example - we have a list of Geonames (geographical locations from geonames.org).
The filter ignores geonames which do not pass the defined logic.

    List<Geoname> filteredGeonames = GeonameHandler.processCsvFile("/home/uwe/development/data/geonames/DE.txt",0)
        .stream()
        .filter(geoname -> logic.evaluate(geoname).passed())
        .toList();

Or you could e.g. check if the results of all objects is "passed":
    
    boolean result = GeonameHandler.processCsvFile("/home/uwe/development/data/geonames/DE.txt",0)
        .stream()
        .map(logic::evaluate)
        .allMatch(EvaluationResult::passed);

The same is valid of course when you want to check all failed ones.

Instead of using a string array like in the case above, you can use any other object that you want to test against a defined logic. Like
e.g. a record from a SQL resultset or a row that contains all fields parsed from a CSV file. Using an object that represents a row of attributes
or fields will allow you to compare fields against each other.

You may use the getGroupConnectionLogic() method to retrieve a string representation of the logic and how groups and checks are evaluated:

    System.out.println("connection logic: " + logic.getGroupConnectionLogic());

Instead of defining lambda expressions for the checks over and over again, you could also put them in a different class or library and use them as
static variables.

### Examples

#### Using a Map
Simple example using a map as the container of the data.

    Map<String,String> testData = new HashMap<>();
    testData.put("name", "Charles");
    testData.put("age","47");

    Logic<Map<String,String>> logic = new Logic.Builder<Map<String,String>>()
            .addGroup(new Group.Builder<Map<String,String>>("group1")
                    .withCheck("name equals", map ->  map.get("name").equals("Charles"))
                    .build())
            .addGroup(new Group.Builder<Map<String,String>>("group2")
                    .withCheck("is greater", map ->  Integer.parseInt(map.get("age")) > 40)
                    .connectorToPreviousGroup(GroupConnectorType.AND)
                    .build())
            .build();

    boolean result = logic.evaluate(testData).passed();

#### Using a Map and Predicate
Simple example using a map as the container of the data and defining the checks as predicates.

    Map<String,String> testData = new HashMap<>();
    testData.put("name", "Charles");
    testData.put("age","47");

    Predicate<Map<String,String>> isCharles = map -> map.get("name").equals("Charles");
    Predicate<Map<String,String>> ageAbove40 = map -> Integer.parseInt(map.get("age")) > 40;

    Logic<Map<String,String>> logic = new Logic.Builder<Map<String,String>>()
            .addGroup(new Group.Builder<Map<String,String>>("group1")
                    .withCheck("name equals", isCharles)
                    .build())
            .addGroup(new Group.Builder<Map<String,String>>("group2")
                    .withCheck("is greater", ageAbove40)
                    .connectorToPreviousGroup(GroupConnectorType.AND)
                    .build())
            .build();

    boolean result = logic.evaluate(testData).failed();

### Summary

Using groups with AND or OR connectors between the individual checks, as well as using AND, OR, AND_NOT, OR_NOT between the individual groups
you can build very complex logic in a simple and structured way, without the need to use lots of brackets or complicated designs with if statements.

last update: Uwe Geercken - 2024/07/21
