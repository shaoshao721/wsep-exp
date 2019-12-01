package cn.hhchat.server.monitor.commons;

/**
 * Created this one by HMH on 2017/6/22.
 */
public class RMsgFactory {

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


//    public static RMsg warn(ResultEnum resultType){
//        return new RMsg<String>(resultType);
//    }
//
//    public static RMsg warn(ResultEnum resultType, String msg){
//        return new RMsg<String>(resultType, msg);
//    }
//
//    public static <T> RMsg warn(ResultEnum resultType, T data){
//        return new RMsg<T>(resultType, data);
//    }
}
