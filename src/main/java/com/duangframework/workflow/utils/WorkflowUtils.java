package com.duangframework.workflow.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author laotang
 * @date 2019/6/11.
 */
public class WorkflowUtils {

    /***
     * 判断传入的对象是否为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,为空或等于0时返回true
     */
    public static boolean isEmpty(Object obj) {
        return checkObjectIsEmpty(obj, true);
    }

    /***
     * 判断传入的对象是否不为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,不为空或不等于0时返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return checkObjectIsEmpty(obj, false);
    }

    @SuppressWarnings("rawtypes")
    private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
        if (null == obj) {
            return bool;
        }
        else if (obj == "" || "".equals(obj)) {
            return bool;
        }
        else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            try {
                Double.parseDouble(obj + "");
            } catch (Exception e) {
                return bool;
            }
        } else if (obj instanceof String) {
            if (((String) obj).length() <= 0) {
                return bool;
            }
            if ("null".equalsIgnoreCase(obj+"")) {
                return bool;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0) {
                return bool;
            }
        }
        return !bool;
    }

//    /**
//     * 创建条件分支的代码，方便根据条件分支值找出对应的流程审批人节点
//     * @param conditionMap 条件内容Map
//     * @return
//     */
//    public static String createProcessCode(Map<String,String> conditionMap) {
//        StringBuilder stringBuilder = new StringBuilder();
//        if(WorkflowUtils.isNotEmpty(conditionMap)) {
//            conditionMap.entrySet().iterator().forEachRemaining(new Consumer<Map.Entry<String, String>>() {
//                @Override
//                public void accept(Map.Entry<String, String> entry) {
//                    stringBuilder.append(entry.getKey() + entry.getValue());
//                }
//            });
//        }
//        try {
//            String conditionString = stringBuilder.toString();
//            if(WorkflowUtils.isNotEmpty(conditionString)) {
//                return Base64.encodeBase64String(conditionString.getBytes(ConstEnums.PROPERTIES.DEFAULT_ENCODING.getValue()));
//            }
//            return "";
//        } catch (Exception e) {
//            logger.warn(e.getMessage(), e);
//            return "";
//        }
//    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符等
     * @param str
     * @return
     */
    public static String replaceSpecialStr4Xml(String str) {
        String repl = "";
        if (str != null) {
            Pattern p = Pattern.compile(">(\\s*|\n|\t|\r)<");
            Matcher m = p.matcher(str);
            repl = m.replaceAll("><");
        }
        return repl;
    }

    /**
     * 深度复制
     * @param src
     * @param <T>
     * @return
     */
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

    /**
     * 通过反射创建实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> commandClass) {
        T instance;
        try {
            instance = (T) commandClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static boolean hasLength(CharSequence str) {
        return isNotEmpty(str) && str.length() > 0;
    }

    public static boolean hasText(CharSequence str) {
        return hasLength(str)  && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 随机生成字符串
     * @param size 位数
     */
    public static String getRandomStr(int size) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            int number = random.nextInt(Const.RANDOM_STR.length());
            sb.append(Const.RANDOM_STR.charAt(number));
        }
        return sb.toString();
    }

    public static String buildSortXml(StringBuilder nodeXmlString, StringBuilder edgeXmlString) {
        StringBuilder xml = new StringBuilder();
        xml.append("<mxGraphModel><root>")
                .append(nodeXmlString)
                .append(edgeXmlString)
                .append("</root></mxGraphModel>");
        return xml.toString();
    }
}
