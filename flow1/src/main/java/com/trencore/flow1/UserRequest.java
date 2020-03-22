package com.trencore.flow1;

import javax.servlet.http.HttpServletRequest;

public class UserRequest  {

    private final HttpServletRequest httpServletRequest;

    public UserRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}
