package com.example.lockfree;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Engine {
    private List<Object> list = new CopyOnWriteArrayList<>();

    public boolean addListener(Object obj) {
        return list.add(obj);
    }

    public void print() {
        for (Object obj : list) {
            System.out.println(obj);
        }
    }
}
