package org.miaohong.fishrpc.core.execption;

public class FrameworkException extends AbstractCoreException {
    private static final long serialVersionUID = 467246479718774695L;

    public FrameworkException() {
        super();
    }

    public FrameworkException(CoreErrorMsg coreErrorMsg) {
        super(coreErrorMsg);
    }

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, CoreErrorMsg coreErrorMsg) {
        super(message, coreErrorMsg);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkException(String message, Throwable cause, CoreErrorMsg coreErrorMsg) {
        super(message, cause, coreErrorMsg);
    }

    public FrameworkException(Throwable cause) {
        super(cause);
    }

    public FrameworkException(Throwable cause, CoreErrorMsg coreErrorMsg) {
        super(cause, coreErrorMsg);
    }

}
