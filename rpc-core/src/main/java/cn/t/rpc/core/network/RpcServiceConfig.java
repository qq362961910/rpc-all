package cn.t.rpc.core.network;

import cn.t.rpc.core.util.RpcConfigTool;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * @description: 服务配置描述
 * create: 2019-09-17 11:05
 * @author: yj
 **/
@ConfigurationProperties(prefix = RpcConfigTool.PREFIX)
public class RpcServiceConfig {

    private ServerConfig server;

    private ZookeeperConfig zookeeper;

    public ServerConfig getServer() {
        return server;
    }

    public void setServer(ServerConfig server) {
        this.server = server;
    }

    public ZookeeperConfig getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZookeeperConfig zookeeper) {
        this.zookeeper = zookeeper;
    }

    public static class ZookeeperConfig {
        private String registerAddress;

        public String getRegisterAddress() {
            return registerAddress;
        }

        public void setRegisterAddress(String registerAddress) {
            this.registerAddress = registerAddress;
        }
    }

    public static class ServerConfig {
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

        @Override
        public String toString() {
            return host + ":" + port;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ServerConfig server = (ServerConfig) o;
            return port == server.port &&
                Objects.equals(host, server.host);
        }

        @Override
        public int hashCode() {
            return Objects.hash(host, port);
        }
    }
}


