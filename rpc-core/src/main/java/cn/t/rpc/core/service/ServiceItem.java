package cn.t.rpc.core.service;

/**
 * @description: service item
 * create: 2019-09-18 21:01
 * @author: yj
 **/
public class ServiceItem {

    private Object ref;

    private String interfaceName;

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

}
