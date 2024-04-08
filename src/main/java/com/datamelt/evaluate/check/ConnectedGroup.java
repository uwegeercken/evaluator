package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;

public class ConnectedGroup
{
    private final Group group;
    private final ConnectorType connectorToPreviousGroup;

    public ConnectedGroup(Group group, ConnectorType connectorTypePreviousGroup)
    {
        this.group = group;
        this.connectorToPreviousGroup = connectorTypePreviousGroup;
    }

    public Group getGroup()
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
