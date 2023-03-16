package com.example.book.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class BeanUtils {

    /**
     * 复制Bean属性并忽略null值属性
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 获取空值属性的名称数组
     *
     * @param source 源对象
     * @return String[]
     */
    private static String[] getNullPropertyNames(Object source) {
        BeanWrapper srcBeanWrapper = new BeanWrapperImpl(source);
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor propertyDescriptor : srcBeanWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            if (srcBeanWrapper.getPropertyValue(propertyName) == null) {
                emptyNames.add(propertyName);
            }
        }
        return emptyNames.toArray(new String[0]);
    }
}
