package edu.dental.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RestRequestReader {

    //TODO temporary

    private final Pattern regExIdPattern;

    public RestRequestReader(String resource) {
        this.regExIdPattern = Pattern.compile(resource + "/(\\d*)");
    }

    public Integer getId(String pathInfo) {
        Matcher matcher;

        matcher = regExIdPattern.matcher(pathInfo);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return -1;
        }
    }
}