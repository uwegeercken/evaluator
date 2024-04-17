package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;

public class ConnectedGroup<T>
{
    private final Group<T> group;
    private final ConnectorType connectorToPreviousGroup;

    public ConnectedGroup(Group<T> group, ConnectorType connectorTypePreviousGroup)
    {
        this.group = group;
        this.connectorToPreviousGroup = connectorTypePreviousGroup;
    }

    public Group<T> getGroup()
    {
        return group;
    }

    public ConnectorType getConnectorToPreviousGroup()
    {
        return connectorToPreviousGroup;
    }

    public String toString()
    {
        return group.getName() + " --> " + getConnectorToPreviousGroup();
    }
}
