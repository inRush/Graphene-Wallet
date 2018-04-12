package com.sxds.common.helper;


import java.io.Serializable;
import java.util.HashMap;

/**
 * @author thewinds
 * @date 2017/12/8
 * 可序列化的 Map<String,String>
 */

public class SerializableHashMap implements Serializable {
    private HashMap<String,Object> map;

    public SerializableHashMap(HashMap<String, Object> map) {
        this.map = map;
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Object> map) {
        this.map = map;
    }
}
