package com.an.lfs.enu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.an.lfs.LfsUtil;

public enum CmpType {
    NULL("NULL"), //
    Z_Z_Z(LfsUtil.Z_Z_Z), //
    Z_Z_A(LfsUtil.Z_Z_A), //
    Z_Z_O(LfsUtil.Z_Z_O), //
    Z_A_Z(LfsUtil.Z_A_Z), //
    Z_A_A(LfsUtil.Z_A_A), //
    Z_A_O(LfsUtil.Z_A_O), //
    Z_O_Z(LfsUtil.Z_O_Z), //
    Z_O_A(LfsUtil.Z_O_A), //
    Z_O_O(LfsUtil.Z_O_O), //
    A_Z_Z(LfsUtil.A_Z_Z), //
    A_Z_A(LfsUtil.A_Z_A), //
    A_Z_O(LfsUtil.A_Z_O), //
    A_A_Z(LfsUtil.A_A_Z), //
    A_A_A(LfsUtil.A_A_A), //
    A_A_O(LfsUtil.A_A_O), //
    A_O_Z(LfsUtil.A_O_Z), //
    A_O_A(LfsUtil.A_O_A), //
    A_O_O(LfsUtil.A_O_O), //
    O_Z_Z(LfsUtil.O_Z_Z), //
    O_Z_A(LfsUtil.O_Z_A), //
    O_Z_O(LfsUtil.O_Z_O), //
    O_A_Z(LfsUtil.O_A_Z), //
    O_A_A(LfsUtil.O_A_A), //
    O_A_O(LfsUtil.O_A_O), //
    O_O_Z(LfsUtil.O_O_Z), //
    O_O_A(LfsUtil.O_O_A), //
    O_O_O(LfsUtil.O_O_O); //

    public static List<CmpType> cmpTypeList = new ArrayList<>();
    public static Map<String, CmpType> cmpTypeMap = new HashMap<>();
    static {
        cmpTypeList.add(Z_Z_Z);
        cmpTypeList.add(Z_Z_A);
        cmpTypeList.add(Z_Z_O);
        cmpTypeList.add(Z_A_Z);
        cmpTypeList.add(Z_A_A);
        cmpTypeList.add(Z_A_O);
        cmpTypeList.add(Z_O_Z);
        cmpTypeList.add(Z_O_A);
        cmpTypeList.add(Z_O_O);
        cmpTypeList.add(A_Z_Z);
        cmpTypeList.add(A_Z_A);
        cmpTypeList.add(A_Z_O);
        cmpTypeList.add(A_A_Z);
        cmpTypeList.add(A_A_A);
        cmpTypeList.add(A_A_O);
        cmpTypeList.add(A_O_Z);
        cmpTypeList.add(A_O_A);
        cmpTypeList.add(A_O_O);
        cmpTypeList.add(O_Z_Z);
        cmpTypeList.add(O_Z_A);
        cmpTypeList.add(O_Z_O);
        cmpTypeList.add(O_A_Z);
        cmpTypeList.add(O_A_A);
        cmpTypeList.add(O_A_O);
        cmpTypeList.add(O_O_Z);
        cmpTypeList.add(O_O_A);
        cmpTypeList.add(O_O_O);

        cmpTypeMap.put(LfsUtil.A_A_A, A_A_A);
        cmpTypeMap.put(LfsUtil.A_A_Z, A_A_Z);
        cmpTypeMap.put(LfsUtil.A_A_O, A_A_O);
        cmpTypeMap.put(LfsUtil.A_Z_A, A_Z_A);
        cmpTypeMap.put(LfsUtil.A_Z_Z, A_Z_Z);
        cmpTypeMap.put(LfsUtil.A_Z_O, A_Z_O);
        cmpTypeMap.put(LfsUtil.A_O_A, A_O_A);
        cmpTypeMap.put(LfsUtil.A_O_Z, A_O_Z);
        cmpTypeMap.put(LfsUtil.A_O_O, A_O_O);
        cmpTypeMap.put(LfsUtil.Z_A_A, Z_A_A);
        cmpTypeMap.put(LfsUtil.Z_A_Z, Z_A_Z);
        cmpTypeMap.put(LfsUtil.Z_A_O, Z_A_O);
        cmpTypeMap.put(LfsUtil.Z_Z_A, Z_Z_A);
        cmpTypeMap.put(LfsUtil.Z_Z_Z, Z_Z_Z);
        cmpTypeMap.put(LfsUtil.Z_Z_O, Z_Z_O);
        cmpTypeMap.put(LfsUtil.Z_O_A, Z_O_A);
        cmpTypeMap.put(LfsUtil.Z_O_Z, Z_O_Z);
        cmpTypeMap.put(LfsUtil.Z_O_O, Z_O_O);
        cmpTypeMap.put(LfsUtil.O_A_A, O_A_A);
        cmpTypeMap.put(LfsUtil.O_A_Z, O_A_Z);
        cmpTypeMap.put(LfsUtil.O_A_O, O_A_O);
        cmpTypeMap.put(LfsUtil.O_Z_A, O_Z_A);
        cmpTypeMap.put(LfsUtil.O_Z_Z, O_Z_Z);
        cmpTypeMap.put(LfsUtil.O_Z_O, O_Z_O);
        cmpTypeMap.put(LfsUtil.O_O_A, O_O_A);
        cmpTypeMap.put(LfsUtil.O_O_Z, O_O_Z);
        cmpTypeMap.put(LfsUtil.O_O_O, O_O_O);
    }

    public static CmpType getCmpType(String val) {
        if (cmpTypeMap.containsKey(val)) {
            return cmpTypeMap.get(val);
        }
        return CmpType.NULL;
    }

    private String val;

    private CmpType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}