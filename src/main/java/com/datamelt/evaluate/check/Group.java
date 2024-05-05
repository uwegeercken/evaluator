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
    private final ConnectorType connectorTypeChecks;
    private final ConnectorType connectorTypePreviousGroup;

    private Group(Builder<T> builder)
    {
        this.name = builder.name;
        this.connectorTypeChecks = builder.connectorTypeChecks;
        this.connectorTypePreviousGroup = builder.connectorTypePreviousGroup;

        this.checks = builder.checks;
    }

    public String getName()
    {
        return name;
    }

    public ConnectorType getConnectorTypeChecks()
    {
        return connectorTypeChecks;
    }

    public ConnectorType getConnectorTypePreviousGroup()
    {
        return connectorTypePreviousGroup;
    }

    public List<Check<T>> getChecks() { return checks; }

    public List<CheckResult<T>> getCheckResults(T dataObject)
    {
        return checks.stream()
                .map(check -> new CheckResult<>(check, check.evaluate(dataObject)))
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
        private ConnectorType connectorTypeChecks = ConnectorType.AND;
        private ConnectorType connectorTypePreviousGroup = ConnectorType.AND;

        public Builder(String name)
        {
            this.name = name;
        }

        public Builder<T> connectingChecksUsing(ConnectorType connectorType)
        {
            this.connectorTypeChecks = connectorType;
            return this;
        }

        public Builder<T> withCheck(String name, Predicate<T> predicate)
        {
            checks.add(new Check<>(name, predicate));
            return this;
        }

        public Builder<T> connectingToPreviousGroupUsing(ConnectorType connectorToPreviousGroup)
        {
            connectorTypePreviousGroup = connectorToPreviousGroup;
            return this;
        }

        public Group<T> build()
        {
            return new Group<>(this);
        }
    }


}
