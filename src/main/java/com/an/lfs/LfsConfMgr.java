package com.an.lfs;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class LfsConfMgr {
    private static final Log logger = LogFactory.getLog(LfsConfMgr.class);

    private LfsConf conf;

    public LfsConfMgr() {
        init();
    }

    public LfsConf getConf() {
        return conf;
    }

    @Override
    public String toString() {
        return "LfsConfMgr [" + (conf != null ? "conf=" + conf : "") + "]";
    }

    private void init() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("Initialize conf.");
            String filepath = LfsUtil.getConfFilePath("lfs.txt");
            File f = new File(filepath);
            logger.info("file: " + f.getPath());
            conf = mapper.readValue(f, LfsConf.class);
            logger.info("conf: " + conf.toString());
        } catch (Exception e) {
            logger.error("Error: " + e);
            throw new RuntimeException("Failed to initialize configuration, error: " + e);
        }
    }
}