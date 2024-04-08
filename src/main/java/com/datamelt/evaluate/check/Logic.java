package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.model.DuplicateElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Logic
{
    private static final Logger logger = LoggerFactory.getLogger(Logic.class);
    private final List<ConnectedGroup> groups;

    private Logic(Builder builder)
    {
        this.groups = builder.groups;
    }

    public List<ConnectedGroup> getGroups()
    {
        return groups;
    }

    private Optional<ConnectedGroup> getGroup(String name)
    {
        return groups.stream()
                .filter(group -> group.getGroup().getName().equals(name))
                .findFirst();
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

        public Logic build()
        {
            return new Logic(this);
        }

        private Optional<ConnectedGroup> getGroup(String name)
        {
            return groups.stream()
                    .filter(group -> group.getGroup().getName().equals(name))
                    .findFirst();
        }
    }
}
