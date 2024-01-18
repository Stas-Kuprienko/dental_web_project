package edu.dental.service.tools;

import jakarta.servlet.ServletException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RestRequestReader {

    private final Pattern regExIdPattern;

    public RestRequestReader(String resource) {
        this.regExIdPattern = Pattern.compile(resource + "/(\\d*)");
    }

    public Integer getId(String pathInfo) throws ServletException {
        Matcher matcher;

        matcher = regExIdPattern.matcher(pathInfo);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new ServletException("Invalid URI");
    }
}