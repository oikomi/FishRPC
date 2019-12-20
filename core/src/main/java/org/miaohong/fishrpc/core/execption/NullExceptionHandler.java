package org.miaohong.fishrpc.core.execption;

public class NullExceptionHandler<T extends Exception> implements ExceptionHandler<T> {
    @Override
    public <S extends Exception> T handle(S e) {
        return null;
    }
}
