package com.trencore.flow1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestMapping {

    class URLAndMethod {
        HttpMethod httpMethod;
        String url;

        public URLAndMethod(HttpMethod httpMethod, String url) {
            this.httpMethod = httpMethod;
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            URLAndMethod that = (URLAndMethod) o;
            return httpMethod == that.httpMethod &&
                    Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(httpMethod, url);
        }
    }

    Map<URLAndMethod, Controller<HttpServletRequest>> mapping = new HashMap<>();

    public Controller<HttpServletRequest> serve(HttpMethod httpMethod, String url, HttpServletRequest request, HttpServletResponse response) {
        URLAndMethod urlAndMethod = new URLAndMethod(httpMethod, url);
        Controller<HttpServletRequest> controller = new Controller<>();
        mapping.put(urlAndMethod, controller);
        return controller;
    }
}
