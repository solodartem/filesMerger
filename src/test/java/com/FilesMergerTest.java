package com;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FilesMergerTest {

    private void validateIteration(FilesMerger filesMerger, int expectedFilesCount, String expectedDate, int expectedValue) {
        List<FileIterator> topFiles = filesMerger.findTopFiles();
        assertEquals(expectedFilesCount, topFiles.size());
        Record record = filesMerger.mergeFileRecords(topFiles);
        assertEquals(expectedDate, Record.DATE_PARSER.format(record.getDate()));
        assertEquals(expectedValue, record.getValue());
    }

    @Test
    public void findTopFiles() {
        try (FilesMerger filesMerger = new FilesMerger()) {
            assertTrue(filesMerger.initFiles("outFile.txt", new String[]{"file1.txt", "file2.txt", "file3.txt"}));
            validateIteration(filesMerger, 2,"2018-01-01", 11);
            validateIteration(filesMerger, 2,"2018-01-02", 7);
            validateIteration(filesMerger, 1,"2018-01-03", 3);
            validateIteration(filesMerger, 1,"2018-01-04", 4);
            validateIteration(filesMerger, 3,"2018-01-05", 56);
        }
    }
}