package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private String hostname;
    private int port;
    private ServerSocket serverSocket;
    private CopyOnWriteArrayList<Channel> channels;
    private CopyOnWriteArrayList<String> userNames;

    public Server(){
        this.hostname = AddressInfo.addr;
        this.port = AddressInfo.port;
        try {
            this.serverSocket = new ServerSocket(port);
            this.channels = new CopyOnWriteArrayList<>();
            this.userNames = new CopyOnWriteArrayList<>();
        } catch (IOException e) {
            System.out.println("init error!");
            e.printStackTrace();
        }
    }

    /**
     * サーバーを起動する
     */
    public void start(){
        while (true){
            try {
                Socket client = serverSocket.accept();
                Channel channel = new Channel(client,this);
                channels.add(channel);
                userNames.add(channel.getName());
                new Thread(channel).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * サーバーを停止する
     */
    public void stop(){

    }

    public CopyOnWriteArrayList<Channel> getChannels() {
        return channels;
    }

    public CopyOnWriteArrayList<String> getUserNames() {
        return userNames;
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
