package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Group<T>
{
    private static final Logger logger = LoggerFactory.getLogger(Group.class);
    private final List<Check<T>> checks;
    private final String name;
    private final ConnectorType connectorBetweenChecks;

    public Group(Builder<T> builder)
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

    public boolean evaluateChecks(T dataObject)
    {
        List<Boolean> results = checks.stream()
                .map(checks -> checks.evaluate(dataObject))
                .toList();
        boolean combinedChecksResults = getCombinedChecksResults(results).orElse(false);
        logger.debug("results of all checks for group [{}] using connector [{}] --> [{}]", name, connectorBetweenChecks, combinedChecksResults);
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

    public static class Builder<T>
    {
        private final List<Check<T>> checks = new ArrayList<>();
        private final String name;
        private ConnectorType connectorType = ConnectorType.AND;

        public Builder(String name)
        {
            this.name = name;
        }

        public Builder<T> connectingChecksUsing(ConnectorType connectorType)
        {
            this.connectorType = connectorType;
            return this;
        }

        public Builder<T> addCheck(Check<T> check)
        {
            checks.add(check);
            return this;
        }

        public Group<T> build()
        {
            return new Group<>(this);
        }
    }
}
