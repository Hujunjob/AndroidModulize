package com.hujun.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by junhu on 2020/3/30
 */
public class EmptyUtils {
    public static boolean isEmpty(CharSequence cs){return cs==null || cs.length()==0;}

    public static boolean isEmpty(Collection<?> c){return c==null||c.isEmpty();}

    public static boolean isEmpty(Map<?,?> map){return map==null||map.isEmpty();}
}
