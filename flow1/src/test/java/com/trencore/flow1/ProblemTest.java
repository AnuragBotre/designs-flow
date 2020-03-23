package com.trencore.flow1;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ProblemTest {

    @Test
    public void when_RequestArrived_Then_ReturnStringResponse() {

        HttpServletRequest request = null;
        HttpServletResponse response = null;

        RequestMapping requestMapping = new RequestMapping();

        requestMapping
                .serve(HttpMethod.GET, "/app1", request, response)
                .map(httpServletRequest -> new UserRequest(httpServletRequest))
                .businessLogic((connection, userRequest) -> {
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user");
                        ResultSet resultSet = preparedStatement.executeQuery();
                        List list = new ArrayList();
                        while (resultSet.next()) {
                            User user = new User();
                            list.add(user);
                        }
                        return list;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });


        //how databases layer can be made functional
    }

    public void when_multiple_fetch_is_required_then_it_should_return_functional_interface() {
        QueryBuilder queryBuilder = new QueryBuilder();
        Either<ArrayList, Exception> result = fetch("SELECT * FROM user where username = ? and age = ?",
                array("John", 23),
                () -> new ArrayList(), (arrayList, resultSet) -> {
                    User user = new User();
                    arrayList.add(user);
                }
        );

        /*result.ifSuccess(arrayList -> {

        }).orFailure(e -> e.printStackTrace());*/
    }

    private Object[] array(Object... args) {
        return args;
    }


    private <T> Either<T, Exception> fetch(String sql, Object[] args, Supplier<T> supplier, BiConsumer<T, ResultSet> biConsumer) {
        try (Connection connection = getConnection()) {
            return preparedStatement(sql, args, supplier, biConsumer, connection);
        } catch (Exception e) {
            Either either = new Either<>(null,e);
            return either;
        }
    }

    private <T> Either<T, Exception> preparedStatement(String sql, Object[] args, Supplier<T> supplier, BiConsumer<T, ResultSet> biConsumer, Connection connection) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            Either<T, Exception> resultSet = getResultSet(supplier, preparedStatement, biConsumer);
            return resultSet;
        } catch (Exception e) {
            Either either = new Either<>(null,e);
            return either;
        }
    }

    private <T> Either<T, Exception> getResultSet(Supplier<T> supplier, PreparedStatement preparedStatement, BiConsumer<T, ResultSet> biConsumer) {
        try (ResultSet resultSet = preparedStatement.executeQuery();) {
            T t = null;

            while (resultSet.next()) {
                t = supplier.get();
                biConsumer.accept(t, resultSet);
                break;
            }

            while (resultSet.next()) {
                biConsumer.accept(t, resultSet);
            }

            Optional<T> t1 = Optional.ofNullable(t);
            Either either = new Either(t1,null);
            return either;

        } catch (Exception e) {
            Either either = new Either<>(null,e);
            return either;
        }
    }

    private Connection getConnection() {
        return null;
    }
}