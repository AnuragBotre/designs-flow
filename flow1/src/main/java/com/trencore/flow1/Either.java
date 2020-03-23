package com.trencore.flow1;

import java.util.function.Consumer;

public class Either<RESULT, EXCEPTION extends Throwable> {

    private RESULT result;
    private EXCEPTION exception;

    public Either(RESULT left, EXCEPTION right) {
        this.result = left;
        this.exception = right;
    }

    public interface Failure<EXCEPTION extends Throwable> {
        void orFailure(Consumer<EXCEPTION> consumer);
    }

    private class FailureImpl implements Failure<EXCEPTION> {

        EXCEPTION exception;

        public FailureImpl() {
        }

        public FailureImpl(EXCEPTION exception) {
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
            return new FailureImpl(exception);
        }
    }

    class NoOpFailure extends FailureImpl {


        @Override
        public void orFailure(Consumer consumer) {

        }
    }
}
