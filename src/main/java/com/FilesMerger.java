package com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilesMerger implements AutoCloseable {

    private final static Logger LOGGER = Logger.getLogger(FilesMerger.class.getName());

    private BufferedWriter bw = null;

    private List<FileIterator> fileIterators;

    public boolean initFiles(String outFile, String[] files) {
        this.fileIterators = new ArrayList<>(files.length);
        for (String file : files) {
            FileIterator fileIterator = FileIterator.openFile(file);
            if (fileIterator == null) {
                LOGGER.info("Skipped file: " + file);
            } else {
                fileIterators.add(fileIterator);
            }
        }

        if (fileIterators.isEmpty()) {
            return false;
        }

        try {
            File file = new File(FilesMerger.class.getClassLoader().getResource(".").getFile() + "\\" + outFile);
            LOGGER.info("Merged data will be saved to file:" + file.getAbsolutePath());
            this.bw = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public List<FileIterator> findTopFiles() {
        Map<Date, List<FileIterator>> map = new HashMap<>();
        for (FileIterator fileIterator : this.fileIterators) {
            if (fileIterator.get() != null) {
                map.putIfAbsent(fileIterator.get().getDate(), new LinkedList<>());
                map.get(fileIterator.get().getDate()).add(fileIterator);
            }
        }

        Optional<Date> maxDate = map.keySet().stream().sorted().findFirst();
        if (maxDate.isPresent()) {
            return map.get(maxDate.get());
        }
        return null;
    }

    public Record mergeFileRecords(List<FileIterator> files) {
        Record record = new Record();
        record.setDate(files.get(0).get().getDate());
        Integer mergedValue = 0;
        for (FileIterator file : files) {
            mergedValue += file.get().getValue();
            file.pop();
        }
        record.setValue(mergedValue);
        return record;
    }

    public void mergeFiles() throws IOException {
        List<FileIterator> topFiles = findTopFiles();
        while (topFiles != null) {
            this.bw.write(mergeFileRecords(topFiles).toString());
            this.bw.newLine();
            topFiles = findTopFiles();
        }
    }

    @Override
    public void close() {
        for (FileIterator fileIterator : fileIterators) {
            try {
                fileIterator.close();
            } catch (Exception e) {
                LOGGER.log(Level.ALL, e.getMessage());
            }
        }

        try {
            this.bw.close();
        } catch (IOException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }
}
