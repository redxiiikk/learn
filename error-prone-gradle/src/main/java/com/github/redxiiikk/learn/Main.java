package com.github.redxiiikk.learn;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Short> s = new HashSet<>();
        for (short i = 0; i < 100; i++) {
            s.add(i);
            s.remove(i - 1);
        }
        System.out.println(s.size());
    }

    private static String hello(final String name) {
        if (name.length() == 0) {
            return "Hello, World";
        }

        return "Hello, " + name;
    }
}
