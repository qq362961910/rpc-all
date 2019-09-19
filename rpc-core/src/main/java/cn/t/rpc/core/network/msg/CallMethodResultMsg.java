package cn.t.rpc.core.network.msg;

/**
 * @description: call method result
 * create: 2019-09-18 15:25
 * @author: yj
 **/
public class CallMethodResultMsg {

    private Long id;

    private Object result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public CallMethodResultMsg() {
    }

    public CallMethodResultMsg(long id, Object result) {
        this.id = id;
        this.result = result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CallMethodResultMsg{");
        sb.append("id=").append(id);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}
