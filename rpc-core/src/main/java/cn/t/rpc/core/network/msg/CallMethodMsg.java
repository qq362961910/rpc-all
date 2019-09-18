package cn.t.rpc.core.network.msg;

/**
 * @description: 调用方法实体类
 * create: 2019-09-18 14:09
 * @author: yj
 **/
public class CallMethodMsg {
    private long id;
    private String interfaceName;
    private String methodName;
    private Object arg;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getArg() {
        return arg;
    }

    public void setArg(Object arg) {
        this.arg = arg;
    }
}
