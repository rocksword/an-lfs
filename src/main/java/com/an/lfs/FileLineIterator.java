package com.an.lfs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileLineIterator {
    private String filepath;
    private BufferedReader br;

    /**
     * @param filepath
     *            absolute path
     */
    public FileLineIterator(String filepath) {
        this.filepath = filepath;
    }

    public String nextLine() {
        String line = null;
        try {
            if (br == null) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(filepath), "GBK");
                br = new BufferedReader(read);
            }
            line = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }

    public void close() {
        System.out.println("Close BufferedReader " + br);
        if (br != null) {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
