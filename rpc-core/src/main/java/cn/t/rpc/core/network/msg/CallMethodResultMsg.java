package cn.t.rpc.core.network.msg;

/**
 * @description: call method result
 * create: 2019-09-18 15:25
 * @author: yj
 **/
public class CallMethodResultMsg {

    private long id;

    private String interfaceName;

    private String methodName;

    private Object result;

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
