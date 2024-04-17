package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;
import com.datamelt.evaluate.model.DuplicateElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Logic<T>
{
    private static final Logger logger = LoggerFactory.getLogger(Logic.class);
    private final List<ConnectedGroup<T>> groups;

    private Logic(Builder<T> builder)
    {
        this.groups = builder.groups;
    }

    public List<ConnectedGroup<T>> getGroups()
    {
        return groups;
    }

    private Optional<ConnectedGroup<T>> getGroup(String name)
    {
        return groups.stream()
                .filter(group -> group.getGroup().getName().equals(name))
                .findFirst();
    }

    public static class Builder<T>
    {
        private final List<ConnectedGroup<T>> groups = new ArrayList<>();

        public Builder<T> addGroup(Group<T> group, ConnectorType connectorToPreviousGroup)
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

        public Builder<T> addGroup(Group<T> group)
        {
            addGroup(group, ConnectorType.AND);
            return this;
        }

        public Logic<T> build()
        {
            return new Logic<>(this);
        }

        private Optional<ConnectedGroup<T>> getGroup(String name)
        {
            return groups.stream()
                    .filter(group -> group.getGroup().getName().equals(name))
                    .findFirst();
        }
    }
}
