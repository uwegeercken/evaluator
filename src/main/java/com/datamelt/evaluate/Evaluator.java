package com.datamelt.evaluate;

import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.model.DuplicateElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Evaluator
{
    private static final Logger logger = LoggerFactory.getLogger(Evaluator.class);
    private final List<ConnectedGroup> groups;

    private Evaluator(Builder builder)
    {
        this.groups = builder.groups;
    }

    public List<ConnectedGroup> getGroups()
    {
        return groups;
    }

    public boolean evaluateGroupChecks(String name)
    {
        return getGroup(name).orElseThrow().getGroup().evaluateChecks();
    }

    private Optional<ConnectedGroup> getGroup(String name)
    {
        return groups.stream()
                .filter(group -> group.getGroup().getName().equals(name))
                .findFirst();
    }

    public boolean evaluateGroupChecks()
    {
        List<String> connectors = groups.stream()
                .map(connectedGroup -> connectedGroup.toString())
                .toList();

        boolean result = groups.stream()
                .map(connectedGroup -> new GroupEvaluationResult(connectedGroup.getGroup().evaluateChecks(), connectedGroup.getConnectorToPreviousGroup()))
                .reduce(GroupResultCombiner::combineResults).map(groupEvaluationResult -> groupEvaluationResult.getPassed()).orElse(false);
        logger.debug("combined results of all groups using connector to previous group {} --> [{}]", connectors, result);
        return result;
    }

    public static class Builder
    {
        private final List<ConnectedGroup> groups = new ArrayList<>();

        public Builder addGroup(Group group, ConnectorType connectorToPreviousGroup)
        {
            if(getGroup(group.getName()).isPresent())
            {
                throw new DuplicateElementException(String.format("group with same name [%s] was already added", group.getName()));
            }
            else
            {
                groups.add(new ConnectedGroup(group, connectorToPreviousGroup));
            }
            return this;
        }

        public Builder addGroup(Group group)
        {
            addGroup(group, ConnectorType.AND);
            return this;
        }

        public Evaluator build()
        {
            return new Evaluator(this);
        }

        private Optional<ConnectedGroup> getGroup(String name)
        {
            return groups.stream()
                    .filter(group -> group.getGroup().getName().equals(name))
                    .findFirst();
        }
    }
}
