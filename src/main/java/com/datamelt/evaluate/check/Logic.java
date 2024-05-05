package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.DuplicateElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Logic<T>
{
    private static final Logger logger = LoggerFactory.getLogger(Logic.class);
    private final List<Group<T>> groups;

    private Logic(Builder<T> builder)
    {
        this.groups = builder.groups;
    }

    public List<Group<T>> getGroups()
    {
        return groups;
    }

    public Group<T> getGroup(String name)
    {
        return groups.stream()
                .filter(group -> group.getName().equals(name))
                .findAny().orElseThrow(()-> new RuntimeException("the specified group [" + name + "] was not found"));
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
            if(i==0)
            {
                groupConnectionLogic
                        .append(groups.get(i).getName())
                        .append(" [")
                        .append(groups.get(i).getCheckConnectionLogic())
                        .append("]");
            }
            else
            {
                groupConnectionLogic
                        .append(" ")
                        .append(groups.get(i).getConnectorTypePreviousGroup())
                        .append(" ")
                        .append(groups.get(i).getName())
                        .append(" [")
                        .append(groups.get(i).getCheckConnectionLogic())
                        .append("]")
                        .append(")");
            }
        }
        return groupConnectionLogic.toString();
    }

    public static class Builder<T>
    {
        private final List<Group<T>> groups = new ArrayList<>();

        public Builder<T> addGroup(Group<T> group) throws DuplicateElementException
        {
            if(getGroup(group.getName()).isPresent())
            {
                throw new DuplicateElementException(String.format("group with same name [%s] was already added", group.getName()));
            }
            else
            {
                groups.add(group);
            }
            return this;
        }

        public Logic<T> build()
        {
            return new Logic<>(this);
        }

        private Optional<Group<T>> getGroup(String name)
        {
            return groups.stream()
                    .filter(group -> group.getName().equals(name))
                    .findFirst();
        }
    }
}
