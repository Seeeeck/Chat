package chat.server;


import java.io.*;
import java.net.Socket;

public class Channel implements Runnable{
    private Server server;
    private Socket client;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String name;
    //サーバーが作動しているか
    private boolean isRunning;


    public Channel(Socket client,Server server){
        this.client = client;
        this.server = server;
        try {
            dis = new DataInputStream(new BufferedInputStream(this.client.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(this.client.getOutputStream()));
            isRunning = true;
            name = receiveMsg();
            sendUserNames();
        } catch (IOException e) {
            release();
            e.printStackTrace();
        }

    }

    private void sendUserNames() {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<server.getUserNames().size();i++){
            System.out.println(server.getUserNames().get(i));
            sb.append(server.getUserNames().get(i));
            if (i!=server.getUserNames().size()-1){
                sb.append(",");
            }
        }
        sendMsg(sb.toString());
    }

    @Override
    public void run() {
        sendMsgToAll("start_chat");
        while (isRunning){
            String msg = receiveMsg();
            if (!msg.trim().equals("")){
                sendMsgToAll(msg);
            }
        }
    }

    /**
     * 受信する
     * @return
     */
    private String receiveMsg(){
        String msg = "";
        try {
            msg = dis.readUTF();
        } catch (IOException e) {
            release();
            e.printStackTrace();
        }
        return msg;
    }


    private void sendMsg(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            release();
            e.printStackTrace();
        }
    }


    private void sendMsgToAll(String msg){
        for (Channel channel:server.getChannels()){
            if (channel != this){
                channel.sendMsg(name+":&:"+msg);
            }
        }
    }


    private void release(){
        isRunning = false;
        sendMsgToAll("stop_chat");
        ChatUtils.close(dos,dis,client);
        server.getChannels().remove(this);
        server.getUserNames().remove(name);
    }

    public String getName() {
        return name;
    }
}
