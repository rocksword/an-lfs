package com.an.lfs.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileLineIterator implements AutoCloseable {
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
            logger.warn("File: " + file.getName() + " not exists.");
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

    public static void writeFile(String filepath, String content) throws IOException {
        File file = new File(filepath);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileOutputStream fos = new FileOutputStream(filepath);
                Writer writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));) {
            writer.write(content);
        }
    }

    @Override
    public void close() throws Exception {
        if (br != null) {
            try {
                logger.debug("Close BufferedReader " + br);
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
