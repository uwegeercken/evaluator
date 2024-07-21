package com.datamelt.evaluate.sample;

import com.datamelt.evaluate.utilities.Row;

import java.util.ArrayList;
import java.util.List;

public class DataSample
{
    private List<Person> sampleData = new ArrayList<>();

    public DataSample()
    {
        createData();
    }

    private void createData()
    {
        sampleData.add(new Person(1,"A.","Frank",30, "Frankfurt"));
        sampleData.add(new Person(2,"B.","Frank",40, "Hamburg"));
        sampleData.add(new Person(3,"C.","Frank",50, "Frankfurt"));
        sampleData.add(new Person(4,"D.","Peter",25, "Hamburg"));
        sampleData.add(new Person(5,"E.","Peter",15, "Frankfurt"));
        sampleData.add(new Person(6,"F.","Peter",10, "Hamburg"));
        sampleData.add(new Person(7,"G.","Maria",40, "Frankfurt"));
        sampleData.add(new Person(8,"H.","Maria",20, "Hamburg"));
        sampleData.add(new Person(9,"I.","Maria",80, "Frankfurt"));
    }

    public List<Person> getSampleData()
    {
        return sampleData;
    }
}
