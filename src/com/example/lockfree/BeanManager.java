package com.example.lockfree;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BeanManager {
    private ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();

    public Object getBean(String key) {
        Object bean = map.get(key);
        if (bean == null) {
            map.putIfAbsent(key, 1);
            bean = map.get(key);
        }
        return bean;
    }
}
