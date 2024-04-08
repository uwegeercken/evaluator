package com.datamelt.evaluate.model;

import com.datamelt.evaluate.check.Logic;

public interface LogicProvider<T>
{
    Logic mapValues(T data);
}
