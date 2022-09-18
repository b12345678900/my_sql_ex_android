package com.example.dcght.mysqlexapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExHelper {
    public static String getMatch(String testString, String regex, int group)
    {
        Pattern p;
        Matcher mc;

        p= Pattern.compile(regex);
        mc=p.matcher(testString);
        if(mc.find()) {
            return mc.group(group);
        }
        return "";
    }
}
