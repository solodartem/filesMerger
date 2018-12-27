package com;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {

    public static final SimpleDateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
    public static final String DELIMETER = ":";

    public static final Integer DATE_IDX = 0;
    public static final Integer VALUE_IDX = 1;

    private Date date;

    private int value;

    public static Record parse(String line) {
        if (line == null) {
            return null;
        }
        String[] fields = line.split(DELIMETER);
        Record record = new Record();
        try {
            record.date = DATE_PARSER.parse(fields[DATE_IDX]);
            record.value = Integer.parseInt(fields[VALUE_IDX]);
            return record;
        } catch (Exception e) {
            return null;
        }
    }

    public static String format(Record record) {
        return DATE_PARSER.format(record.date) + DELIMETER + record.value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Record.format(this);
    }

}
