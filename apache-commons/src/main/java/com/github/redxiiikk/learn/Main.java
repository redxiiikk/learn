package com.github.redxiiikk.learn;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println(StringSubstitutor.replace(
                "TEST ",
                Map.of("test.name", "Tom")
        ));

        System.out.println("Hello world!");
    }
}