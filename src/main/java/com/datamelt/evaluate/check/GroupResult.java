package com.datamelt.evaluate.check;

import com.datamelt.evaluate.model.ConnectorType;

public class GroupResult
{
    private final boolean passed;
    private final ConnectorType connectorType;

    public GroupResult(boolean result, ConnectorType connectorType)
    {
        this.passed = result;
        this.connectorType = connectorType;
    }

    public boolean getPassed()
    {
        return passed;
    }

    public ConnectorType getConnectorType()
    {
        return connectorType;
    }
}
