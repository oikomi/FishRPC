package org.miaohong.fishrpc.core.execption;

public class CoreErrorConstant {

    private static final int OK_STATUS = 0;
    private static final int ERR_STATUS = 1;

    private static final int CONFIG_DEFAULT_ERROR_CODE = 10001;
    public static final CoreErrorMsg CONFIG_DEFAULT_ERROR =
            new CoreErrorMsg(ERR_STATUS, CONFIG_DEFAULT_ERROR_CODE, "config error");
    private static final int SERVER_DEFAULT_ERROR_CODE = 40001;
    public static final CoreErrorMsg SERVER_DEFAULT_ERROR =
            new CoreErrorMsg(ERR_STATUS, SERVER_DEFAULT_ERROR_CODE, "server error");
    private static final int SERVER_PROXY_ERROR_CODE = 40002;
    public static final CoreErrorMsg SERVER_PROXY_ERROR =
            new CoreErrorMsg(ERR_STATUS, SERVER_PROXY_ERROR_CODE, "server proxy error");
    private static final int REGISTER_DEFAULT_ERROR_CODE = 60001;
    public static final CoreErrorMsg REGISTER_DEFAULT_ERROR =
            new CoreErrorMsg(ERR_STATUS, REGISTER_DEFAULT_ERROR_CODE, "register error");

    private static final int METRIC_DEFAULT_ERROR_CODE = 80001;
    public static final CoreErrorMsg METRIC_DEFAULT_ERROR =
            new CoreErrorMsg(ERR_STATUS, METRIC_DEFAULT_ERROR_CODE, "metric error");

}
