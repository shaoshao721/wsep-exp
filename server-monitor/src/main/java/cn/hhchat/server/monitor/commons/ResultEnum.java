package cn.hhchat.server.monitor.commons;

/**
 * Created this one by HMH on 2019/4/21.
 */
public enum ResultEnum {
    Success(0, "ok"),
    UnRegUser(101, "未注册"),
    WrongPassword(102, "密码错误"),
    DupUserReg(103, "重复注册"),
    UnexceptError(-1, "非期望错误"),
    InValidField(203, "参数错误"),

    DataError(301, "数据错误"),
    K8SConnectError(302, "K8S 连接失败"),
    K8SCmdError(302, "K8S 命令错误"),
    ResourceExistedError(303, "资源已经存在"),
    DataQueryError(304, "SQL错误"),
    ;

    private Integer errcode;
    private String errmsg;

    public Integer getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    ResultEnum(Integer errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }
}
