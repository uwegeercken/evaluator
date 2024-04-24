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

    public String getGroupConnectionLogic()
    {
        StringBuilder groupConnectionLogic = new StringBuilder();
        for(int i=0; i< groups.size()-1; i++)
        {
            groupConnectionLogic.append("(");
        }
        for(int i=0; i< groups.size(); i++)
        {
            ConnectedGroup<T> connectedGroup =groups.get(i);
            if(i==0)
            {
                groupConnectionLogic.append(connectedGroup.getGroup().getName())
                        .append(" [checks:")
                        .append(connectedGroup.getGroup().getConnectorBetweenChecks())
                        .append("]");
            }
            else
            {
                groupConnectionLogic.append(" ")
                        .append(connectedGroup.getConnectorToPreviousGroup())
                        .append(" ")
                        .append(connectedGroup.getGroup().getName())
                        .append(" [checks:")
                        .append(connectedGroup.getGroup().getConnectorBetweenChecks())
                        .append("]")
                        .append(")");
            }
        }
        return groupConnectionLogic.toString();
    }

    public static class Builder<T>
    {
        private final List<ConnectedGroup<T>> groups = new ArrayList<>();

        public Builder<T> addGroup(Group<T> group, ConnectorType connectorToPreviousGroup) throws DuplicateElementException
        {
            if(getGroup(group.getName()).isPresent())
            {
                throw new DuplicateElementException(String.format("group with same name [%s] was already added", group.getName()));
            }
            else
            {
                groups.add(new ConnectedGroup<>(group, connectorToPreviousGroup));
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
