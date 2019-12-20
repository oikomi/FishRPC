package org.miaohong.fishrpc.core.execption;

public interface ExceptionHandler<T extends Exception> {
    <S extends Exception> T handle(S e);
}
