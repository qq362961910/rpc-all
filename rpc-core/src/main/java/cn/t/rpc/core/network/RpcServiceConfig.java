package cn.t.rpc.core.network;

import cn.t.rpc.core.util.RpcConfigTool;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 服务配置描述
 * create: 2019-09-17 11:05
 * @author: yj
 **/
@ConfigurationProperties(prefix = RpcConfigTool.PREFIX)
public class RpcServiceConfig {

    private Server server;

    private Zookeeper zookeeper;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Zookeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(Zookeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    public static class Zookeeper {
        private String registerAddress;

        public String getRegisterAddress() {
            return registerAddress;
        }

        public void setRegisterAddress(String registerAddress) {
            this.registerAddress = registerAddress;
        }
    }

    public static class Server {
        private String host;
        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}


