package cn.hhchat.server.monitor.commons;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created this one by HMH on 2019/4/21.
 */
public class RMsg<T> {
    private Integer errCode;
    private String errMsg;
    private T data;
    private Timestamp createTime;

    public RMsg(Integer errCode, String errMsg, T data) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.data = data;
        this.createTime = new Timestamp(new Date().getTime());
    }

    public RMsg() {
    }

    public RMsg(ResultEnum resultEnum) {
        this(resultEnum.getErrcode(), resultEnum.getErrmsg(), null);
    }

    public RMsg(ResultEnum resultEnum, T data) {
        this(resultEnum.getErrcode(), resultEnum.getErrmsg(), data);
    }

    public RMsg(ResultEnum resultEnum, String errmsg, T data) {
        this(resultEnum.getErrcode(), errmsg, data);
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public static RMsg ok() {
        return new RMsg(ResultEnum.Success);
    }

    public static <T> RMsg<T> ok(T data) {
        return new RMsg<>(ResultEnum.Success, data);
    }

    public static <T> RMsg<T> ok(ResultEnum resultType, T data) {
        return new RMsg<>(resultType, data);
    }

    public static RMsg err(ResultEnum resultType) {
        return new RMsg(resultType);
    }

    public static RMsg err(int errcode, String errmsg) {
        return new RMsg(errcode, errmsg, null);
    }

    public static RMsg err(ResultEnum resultType, String errmsg) {
        return new RMsg(resultType, errmsg, null);
    }

    @Override
    public String toString() {
        return "RMsg{" +
                "errCode=" + errCode +
                ", errMessage='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
