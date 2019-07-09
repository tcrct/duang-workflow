package com.duangframework.workflow.utils;

import java.io.*;
import java.util.List;

/**
 * @author laotang
 */
public class Const {

    public static final String START_LABEL = "开始流程";
    public static final String END_LABEL = "结束流程";

    public static final String EDGE_NODE_NAME = "Edge";

    public static final String CC_NODE_FIELD= "cc";

    public static final String TASK_NODE_FIELD = "task";

    public static <T> List<T> deepCopy(List<T> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
