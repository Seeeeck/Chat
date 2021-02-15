package chat.client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private Socket socket;
    private DataOutputStream dos;
    private String name;
    private boolean isRunning;
    private ArrayList<String> users;
    private Display display;

    public Client(){
        isRunning = true;
        users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public Display getDisplay() {
        return display;
    }

    public Socket getSocket() {
        return socket;
    }

    public void startLink(){
        try {
            this.socket = new Socket(AppInfo.addr,AppInfo.port);
            this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            isRunning = true;
            send(name);
        } catch (IOException e) {
            release();
            e.printStackTrace();
        }
    }

    private void createGUI(){
        display = new Display(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setSize(700,500);
        display.setVisible(true);
    }

    private void release(){
        isRunning = false;
        ChatUtils.close(dos,socket);
    }

    public void send(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            release();
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Client client = new Client();
        SwingUtilities.invokeLater(()->client.createGUI());
    }

}
