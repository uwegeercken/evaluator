package com.datamelt.evaluate.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Group<T>
{
    private static final Logger logger = LoggerFactory.getLogger(Group.class);
    private final List<Check<T>> checks;
    private final String name;
    private final CheckConnectorType checkConnectorType;
    private final GroupConnectorType groupConnectorTypePreviousGroup;

    private Group(Builder<T> builder)
    {
        this.name = builder.name;
        this.checkConnectorType = builder.groupConnectorTypeChecks;
        this.groupConnectorTypePreviousGroup = builder.groupConnectorTypePreviousGroup;

        this.checks = builder.checks;
    }

    public String getName()
    {
        return name;
    }

    public GroupConnectorType getConnectorTypeChecks()
    {
        return checkConnectorType;
    }

    public GroupConnectorType getConnectorTypePreviousGroup()
    {
        return groupConnectorTypePreviousGroup;
    }

    public List<Check<T>> getChecks() { return checks; }

    public List<CheckResult> getCheckResults(T dataObject)
    {
        return checks.stream()
                .map(check -> new CheckResult(check.getName(), check.evaluate(dataObject)))
                .toList();
    }

    public String getCheckConnectionLogic()
    {
        return getChecks()
                .stream()
                .map(Check::getName)
                .collect(Collectors.joining(" " + getConnectorTypeChecks().name() + " "));
    }

    public static class Builder<T>
    {
        private final List<Check<T>> checks = new ArrayList<>();
        private final String name;
        private CheckConnectorType checkConnectorType = CheckConnectorType.AND;
        private GroupConnectorType groupConnectorTypePreviousGroup = GroupConnectorType.AND;

        public Builder(String name)
        {
            this.name = name;
        }

        public Builder<T> connectorBetweenChecks(CheckConnectorType checkConnectorType)
        {
            this.checkConnectorType = checkConnectorType;
            return this;
        }

        public Builder<T> withCheck(String name, Predicate<T> predicate)
        {
            checks.add(new Check<>(name, predicate));
            return this;
        }

        public Builder<T> connectorToPreviousGroup(GroupConnectorType connectorToPreviousGroup)
        {
            groupConnectorTypePreviousGroup = connectorToPreviousGroup;
            return this;
        }

        public Group<T> build()
        {
            return new Group<>(this);
        }
    }


}
