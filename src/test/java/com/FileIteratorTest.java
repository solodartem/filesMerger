package com;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class FileIteratorTest {

    @Test
    public void shouldIgnoreBrokenRecords() {

        try (FileIterator fileIterator = FileIterator.openFile("file_with_broke_records.txt")) {
            assertNotNull(fileIterator);
            // 2018-01-05:1
            assertTrue(fileIterator.hasNext());
            Record record = fileIterator.get();
            assertEquals(1, record.getValue());
            assertEquals("2018-01-01", Record.DATE_PARSER.format(record.getDate()));

            //2018-01-02:4
            record = fileIterator.pop();
            assertEquals(4, record.getValue());
            assertEquals("2018-01-05", Record.DATE_PARSER.format(record.getDate()));
            assertFalse(fileIterator.hasNext());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldIgnoreNonExistingFiles() {
        FileIterator fileIterator = FileIterator.openFile("non_existing_file.txt");
        assertNull(fileIterator);
    }

}