package org.miaohong.fishrpc.core.execption;

public class MetricCoreExecption extends AbstractCoreException {

    private static final long serialVersionUID = -3637518921075500537L;

    public MetricCoreExecption() {
        super();
    }

    public MetricCoreExecption(CoreErrorMsg coreErrorMsg) {
        super(coreErrorMsg);
    }

    public MetricCoreExecption(String message) {
        super(message);
    }

    public MetricCoreExecption(String message, CoreErrorMsg coreErrorMsg) {
        super(message, coreErrorMsg);
    }

    public MetricCoreExecption(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricCoreExecption(String message, Throwable cause, CoreErrorMsg coreErrorMsg) {
        super(message, cause, coreErrorMsg);
    }

    public MetricCoreExecption(Throwable cause) {
        super(cause);
    }

    public MetricCoreExecption(Throwable cause, CoreErrorMsg coreErrorMsg) {
        super(cause, coreErrorMsg);
    }

}
