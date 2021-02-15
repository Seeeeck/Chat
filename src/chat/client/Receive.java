package chat.client;

import chat.client.swing.AfPanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Receive implements Runnable{
        private Client client;
        private Socket socket;
        private DataInputStream dis;
        private boolean isRunning;
        private Display display;


        public Receive(Client client,Display display){
            this.client = client;
            this.socket = client.getSocket();
            this.display = display;
            try {
                dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                isRunning = true;
                handleUserNames(receive());
            } catch (IOException e) {
                release();
                e.printStackTrace();
            }
        }

        private void handleUserNames(String userNames){
            String[] userNamesArr = userNames.split(",");
            for (String name:userNamesArr){
                this.client.getUsers().add(name);
            }
        }
        
        private String receive(){
            String msg = "";
            try {
                msg = dis.readUTF();
            } catch (IOException e) {
                release();
                e.printStackTrace();
            }
            return msg;
        }
        
        private void release(){
            isRunning = false;
            ChatUtils.close(dis,socket);
        }

        @Override
        public void run() {
            while (isRunning) {
                String msg = receive();
                System.out.println(msg);
                AfPanel msgPanel = ChatUtils.handleMsg(msg,client);
                display.handleShowMsgArea(msgPanel);
            }
        }
    }