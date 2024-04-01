# evaluator

Library to construct checks on values of two fields using flexible AND and OR conditions.

Checks can be added to groups, where the checks are connected using an AND or an OR condition. Multiple groups can
also be connected using an AND or an OR condition. This way one can create complex logic easily.

The default for the connector between the checks of a group as well as the connector between multiple groups is AND.

Checks have a name and specify two fields plus the logic to apply to the fields. The logic is a lambda expression which
evaluates to a boolean true or false.

Sample:
Two groups are added, each having two checks. The checks of the second group are connected using an OR condition.
There is no 
The evaluateGroupChecks() method returns the result of the evaluation of all checks for all groups.

    Field<Integer> field_01 = new Field<>("f1", 100);
    Field<String> field_03 = new Field<>("f3", "Test 1");
    Field<Integer> field_05 = new Field<>("f5", 100);
    
    Evaluator evaluator = new Evaluator.Builder()
                .addGroup(new Group.Builder("group1")
                        .addCheck(new Check<>("length smaller than", field_03, field_01,(f1, f2) -> f1.getValue().length()< f2.getValue()))
                        .addCheck(new Check<>("equals", field_01, field_05,(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                        .build())
                .addGroup(new Group.Builder("group2")
                        .connectingChecksUsing(ConnectorType.OR)
                        .addCheck(new Check<>("length greater than", field_03, field_01,(f1, f2) -> f1.getValue().length()> f2.getValue()))
                        .addCheck(new Check<>("equals", field_01, field_05,(f1, f2) -> Objects.equals(f1.getValue(), f2.getValue())))
                        .build())
                .build();

    boolean evaluatorResult = evaluator.evaluateGroupChecks();
    System.out.println("evaluator result for all checks and groups: " + evaluatorResult);

last update: Uwe Geercken - 2024/04/01