package server.connection;



import com.jcraft.jsch.*;
import server.Main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.System.exit;

public class Tunnel {
    private String host;
    private String user;
    private String password;
    private int port;
    private String tunnelRemoteHost;
    private int tunnelLocalPort;
    private int tunnelRemotePort;

    public Tunnel(String host,
                  String user,
                  String password,
                  int port,
                  String tunnelRemoteHost,
                  int tunnelLocalPort,
                  int tunnelRemotePort) {

        this.user = user;
        this.password = password;
        this.port = port;
        this.host = host;
        try {
            InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            Main.logger.error("Некорректный ssh-хост.");
            e.printStackTrace();
        }

        this.tunnelRemotePort = tunnelRemotePort;
        this.tunnelRemoteHost = tunnelRemoteHost;
        this.tunnelLocalPort = tunnelLocalPort;
    }

    public int connect() {
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            localUserInfo lui = new localUserInfo();
            session.setUserInfo(lui);
            //CONNECT SSH
            Main.logger.info("Устанавливается ssh подключение к " + host + " : " + port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(3000);
            //DO PORT FORWARDING
            int assingedPort = session.setPortForwardingL(tunnelLocalPort, tunnelRemoteHost, tunnelRemotePort);
            Main.logger.info("Подключение установлено.");
            Main.logger.info("localhost:" + assingedPort + " -> " + tunnelRemoteHost + ":" + tunnelRemotePort);
            return assingedPort;
        } catch (JSchException e) {
            Main.logger.error("Ошибка подключения через ssh-туннель.");
            e.printStackTrace();
        }
        exit(-1);
        return -1;
    }

    class localUserInfo implements UserInfo {
        String passwd;
        public String getPassword() { return passwd; }
        public boolean promptYesNo(String str) { return true; }
        public String getPassphrase() { return null; }
        public boolean promptPassphrase(String message) { return true; }
        public boolean promptPassword(String message) { return true; }
        public void showMessage(String message) { }
    }
}