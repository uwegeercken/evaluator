package com.datamelt.evaluate.utilities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RowTest
{
    @Test
    public void testRowStringObject()
    {
        Row testRow  = new Row.Builder()
                .addField("city", "Frankfurt")
                .build();

        assertEquals("Frankfurt", testRow.getString("city"));
    }

    @Test
    public void testRowLocalDateObject()
    {
        LocalDate birthDate = LocalDate.of(1994, Month.JANUARY,2);
        Row testRow  = new Row.Builder()
                .addField("dob", birthDate)
                .build();

        assertEquals("1994-01-02", testRow.getLocalDate("dob").toString());
    }

    @Test
    public void testRowLocalTimeObject()
    {
        LocalTime lunchTime = LocalTime.of(13,15,0);
        Row testRow  = new Row.Builder()
                .addField("mahlzeit", lunchTime)
                .build();

        assertEquals("13:15:00", testRow.getLocalTime("mahlzeit").format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @Test
    public void testRowCastException()
    {
        Row testRow  = new Row.Builder()
                .addField("name", "Charles")
                .addField("age", 47)
                .addField("city", "Frankfurt")
                .addField("country", "Germany")
                .addField("height", 1.78)
                .build();

        assertThrows(ClassCastException.class, () -> testRow.getString("age"));
    }
}