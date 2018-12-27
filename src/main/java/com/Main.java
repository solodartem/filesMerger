package com;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private final static Logger LOGGER = Logger.getLogger(FilesMerger.class.getName());

    public static void main(String[] args) {
        try (FilesMerger filesMerger = new FilesMerger()) {
            if (filesMerger.initFiles("outFile.txt", new String[]{"file1.txt", "file2.txt", "file3.txt"})) {
                filesMerger.mergeFiles();
            }
        } catch (IOException e){
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }
}
