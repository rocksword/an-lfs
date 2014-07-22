package com.an.lfs.vo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsConst;

public class Category {
    private static final Log logger = LogFactory.getLog(Category.class);
    // 1.5-3-8
    private String name;
    private int passNum;
    private int failNum;

    public String getFirstSeg() {
        String[] strs = name.trim().split(LfsConst.SEPARATOR);
        if (strs.length != 3) {
            logger.error("Invalid " + this.toString());
            return null;
        }
        return strs[0].trim();
    }

    public String getLastSeg() {
        String[] strs = name.trim().split(LfsConst.SEPARATOR);
        if (strs.length != 3) {
            logger.error("Invalid " + this.toString());
            return null;
        }
        return strs[2].trim();
    }

    public void addPassNum() {
        this.passNum++;
    }

    public void addFailNum() {
        this.failNum++;
    }

    public Category(String name) {
        this.name = name;
    }

    public Category() {
    }

    @Override
    public String toString() {
        return "Category [" + (name != null ? "name=" + name + ", " : "") + "passNum=" + passNum + ", failNum="
                + failNum + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }
}
