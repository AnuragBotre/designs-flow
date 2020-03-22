package com.trencore.flow1;

import java.util.function.Consumer;

public class Either<RESULT, EXCEPTION extends Exception> {

    private RESULT result;
    private EXCEPTION exception;

    public Either(RESULT left, EXCEPTION right) {
        this.result = left;
        this.exception = right;
    }

    class Failure<EXCEPTION extends Exception> {

        EXCEPTION exception;

        public Failure() {
        }

        public Failure(EXCEPTION exception) {
            this.exception = exception;
        }

        public void orFailure(Consumer<EXCEPTION> consumer) {
            consumer.accept(exception);
        }
    }

    public <T> Failure<EXCEPTION> ifSuccess(Consumer<RESULT> consumer) {
        if (result != null) {
            consumer.accept(result);
            return new NoOpFailure();
        } else {
            return new Failure<>(exception);
        }
    }

    class NoOpFailure extends Failure {



        @Override
        public void orFailure(Consumer consumer) {

        }
    }
}
