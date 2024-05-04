package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;
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
    private final ConnectorType connectorBetweenChecks;

    private Group(Builder<T> builder)
    {
        this.name = builder.name;
        this.connectorBetweenChecks = builder.connectorType;
        this.checks = builder.checks;
    }

    public String getName()
    {
        return name;
    }

    public ConnectorType getConnectorBetweenChecks()
    {
        return connectorBetweenChecks;
    }

    public List<Check<T>> getChecks() { return checks; }

    public boolean evaluateChecks(T dataObject)
    {
        List<CheckResult<T>> results = getCheckResults(dataObject);

        boolean combinedChecksResults = getCombinedChecksResults(results);
        logger.trace("group [{}] using connector between checks [{}], result [{}]", name, connectorBetweenChecks, combinedChecksResults);
        return combinedChecksResults;
    }

    private List<CheckResult<T>> getCheckResults(T dataObject)
    {
        return checks.stream()
                .map(check -> new CheckResult<>(check, check.evaluate(dataObject)))
                .toList();
    }

    public List<String> test(T dataObject, CheckResultFilterType filterType)
    {
        return getCheckResults(dataObject).stream()
                .filter(CheckResultFilterType.getFilter(filterType))
                .map(CheckResult::toString)
                .toList();
    }

    private boolean getCombinedChecksResults(List<CheckResult<T>> results)
    {
        switch (connectorBetweenChecks)
        {
            case OR ->
            {
                return results.stream()
                        .map(CheckResult::getResult)
                        .reduce((result1, result2) -> result1 || result2).orElse(false);
            }
            case NOR ->
            {
                boolean result =  results.stream()
                        .map(CheckResult::getResult)
                        .reduce((result1, result2) -> (result1 || result2)).orElse(true);
                return !result;
            }
            case NOT ->
            {
                boolean result = results.stream()
                        .map(CheckResult::getResult)
                        .reduce((result1, result2) -> (result1 && result2)).orElse(true);
                return !result;
            }
            default ->
            {
                return results.stream()
                        .map(CheckResult::getResult)
                        .reduce((result1, result2) -> result1 && result2).orElse(false);
            }
        }
    }

    public String getCheckConnectionLogic()
    {
        return getChecks()
                .stream()
                .map(Check::getName)
                .collect(Collectors.joining(" " + getConnectorBetweenChecks().name() + " "));
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

        public Builder<T> addCheck(String name, Predicate<T> predicate)
        {
            checks.add(new Check<>(name, predicate));
            return this;
        }

        public Group<T> build()
        {
            return new Group<>(this);
        }
    }
}
