package com.hujun.modulize.annotation;

import javax.lang.model.element.Element;

/**
 * Created by junhu on 2020/3/30
 * PathBean的升级版
 */
public class RouterBean {
    public enum Type {
        ACTIVITY
    }

    //枚举类型
    private Type type;

    //类节点
    private Element element;

    //被ARouter注解的类对象
    private Class<?> clazz;

    //路由在组名
    private String group;

    //路由的地址
    private String path;

    private RouterBean(Builder builder) {
        this.group = builder.group;
        this.path = builder.path;
        this.element = builder.element;
    }

    private RouterBean(Type type, Class<?> clazz, String path, String group) {
        this.type = type;
        this.clazz = clazz;
        this.path = path;
        this.group = group;
    }

    public Type getType() {
        return type;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getGroup() {
        return group;
    }

    public String getPath() {
        return path;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static RouterBean create(Type type, Class<?> clazz, String path, String group) {
        return new RouterBean(type, clazz, path, group);
    }

    @Override
    public String toString() {
        return "group=" + group + ",path=" + path;
    }

    public static class Builder {

        //类节点
        private Element element;

        //路由在组名
        private String group;

        //路由的地址
        private String path;

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public RouterBean build() {
            if (path == null || path.length() == 0) {
                throw new RuntimeException("需要设置path");
            }
            return new RouterBean(this);
        }
    }
}
