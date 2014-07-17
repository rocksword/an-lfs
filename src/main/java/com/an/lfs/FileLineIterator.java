package com.an.lfs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileLineIterator {
    private static final Log logger = LogFactory.getLog(FileLineIterator.class);
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
        File file = new File(filepath);
        if (!file.exists()) {
            logger.info("File: " + file.getName() + " not exists.");
            return null;
        }

        String line = null;
        try {
            if (br == null) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(filepath), "UTF-8");
                br = new BufferedReader(read);
            }
            line = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }

    public void close() {
        if (br != null) {
            try {
                logger.info("Close BufferedReader " + br);
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void createFile(String filename) {
        String filepath = LfsUtil.getOutputFilePath(filename);
        logger.info("filepath: " + filepath);
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String filename) {
        String filepath = LfsUtil.getOutputFilePath(filename);
        logger.info("filepath: " + filepath);
        try (FileOutputStream fos = new FileOutputStream(filepath);
                Writer writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));) {
            writer.write("中");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
