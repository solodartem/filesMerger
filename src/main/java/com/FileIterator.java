package com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileIterator implements AutoCloseable {

    private BufferedReader br;

    private String line;

    private Record currentRecord;

    private Record nextRecord;

    public static FileIterator openFile(String file) {
        if (FileIterator.class.getClassLoader().getResource(file) != null)
            try {
                BufferedReader br = new BufferedReader(new FileReader(FileIterator.class.getClassLoader().getResource(file).getFile()));
                return new FileIterator(br);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        return null;
    }

    private void readLine() {
        try {
            this.line = this.br.readLine();
        } catch (IOException e) {
            this.line = null;
        }
    }

    public FileIterator(BufferedReader br) {
        this.br = br;
        this.currentRecord = findNextValidRecord();
        this.nextRecord = findNextValidRecord();
    }

    private Record findNextValidRecord() {
        Record record;
        do {
            readLine();
            record = Record.parse(this.line);
        } while (record == null && this.line != null);
        return record;
    }

    public boolean hasNext() {
        return this.nextRecord != null;
    }

    public Record get() {
        return this.currentRecord;
    }

    public Record pop() {
        this.currentRecord = this.nextRecord;
        this.nextRecord = findNextValidRecord();
        return get();
    }

    @Override
    public void close() throws Exception {
        this.br.close();
    }
}
