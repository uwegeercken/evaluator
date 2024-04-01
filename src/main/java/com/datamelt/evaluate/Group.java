package com.datamelt.evaluate;

import com.datamelt.evaluate.model.ConnectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Group
{
    private static final Logger logger = LoggerFactory.getLogger(Group.class);
    private final List<Check<?,?>> checks;
    private final String name;
    private final ConnectorType connectorBetweenChecks;

    public Group(Builder builder)
    {
        this.name = builder.name;
        this.connectorBetweenChecks = builder.connectorType;
        this.checks = builder.checks;
    }

    public String getName()
    {
        return name;
    }

    public ConnectorType getCondition()
    {
        return connectorBetweenChecks;
    }

    public boolean evaluateChecks()
    {
        List<Boolean> results = checks.stream()
                .map(Check::evaluate)
                .toList();
        boolean combinedChecksResults = getCombinedChecksResults(results).orElse(false);
        logger.debug("combined results of all checks for group [{}] using connector [{}] --> [{}]", name, connectorBetweenChecks, combinedChecksResults);
        return combinedChecksResults;
    }

    private Optional<Boolean> getCombinedChecksResults(List<Boolean> results)
    {
        switch (connectorBetweenChecks)
        {
            case AND ->
            {
                return results.stream()
                        .reduce((result1, result2) -> result1 && result2);
            }
            case OR ->
            {
                return results.stream()
                        .reduce((result1, result2) -> result1 || result2);
            }
            default -> throw new IllegalStateException("unexpected value for connector type: " + connectorBetweenChecks);
        }
    }

    public static class Builder
    {
        private final List<Check<?,?>> checks = new ArrayList<>();
        private final String name;
        private ConnectorType connectorType = ConnectorType.AND;

        public Builder(String name)
        {
            this.name = name;
        }

        public Builder connectingChecksUsing(ConnectorType connectorType)
        {
            this.connectorType = connectorType;
            return this;
        }

        public Builder addCheck(Check<?,?> check)
        {
            checks.add(check);
            return this;
        }

        public Group build()
        {
            return new Group(this);
        }
    }
}
