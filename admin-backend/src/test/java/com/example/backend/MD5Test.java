package com.example.backend;

import com.example.backend.common.utils.MD5Utils;


public class MD5Test {
    public static void main(String[] args) {
        String originalString = "Hello, World!";
        System.out.println("Original String: " + originalString);
        System.out.println("MD5 Hash: " + MD5Utils.getMD5(originalString));
        System.out.println("MD5 Hash: " + MD5Utils.getMD5(originalString).substring(0, 16));
    }
}
