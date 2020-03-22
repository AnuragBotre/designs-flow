package com.trencore.flow1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Controller<T> {

    private T t;

    public void getContent() {

    }

    public <R> Controller<R> map(Function<T, R> function) {
        return null;
    }

    public <R> void businessLogic(BiFunction<Connection, T, R> loadFunction) {

    }
}
