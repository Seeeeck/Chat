package chat.client;

import chat.client.swing.AfPanel;
import chat.client.swing.layout.AfSimpleLayout;
import chat.client.swing.layout.AfXLayout;
import chat.client.swing.layout.AfYLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.Closeable;
import java.io.IOException;

public class ChatUtils {
    /**
     * 关闭io流
     */
    public static void close(Closeable... targets){
        for(Closeable target:targets){
            try{
                if(null!=target){
                    target.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static AfPanel handleMsg(String msg,Client client){
        if(msg.endsWith("\n")){
            msg = msg.substring(0,msg.length()-1);
        }
        String[] subMsg = msg.split(":&:");
        switch (subMsg[1]){
            case "start_chat":
                addUserName(subMsg[0],client);
                return createStartMsg(subMsg[0]);
            case "stop_chat":
                removeUserName(subMsg[0],client);
                return createEndMsg(subMsg[0]);
            default:
                return createUserMsg(subMsg);
        }
    }

    private static void addUserName(String name,Client client){
        client.getDisplay().addNameToOnlineUsersBar(name);
    }

    private static void removeUserName(String name,Client client){
        client.getDisplay().removeNameToOnlineUsersBar(name);
    }

    private static AfPanel createStartMsg(String name){
        AfPanel msgPanel = new AfPanel();
        msgPanel.padding(5);
        JLabel msg = new JLabel(name+" がチャットルームに入りました。");
        msg.setForeground(Color.darkGray);
        msgPanel.setLayout(new BorderLayout());
        msgPanel.add(msg,BorderLayout.CENTER);
        return msgPanel;

    }

    private static AfPanel createEndMsg(String name){
        AfPanel msgPanel = new AfPanel();
        msgPanel.padding(5);
        JLabel msg = new JLabel(name+" がチャットルームを出ました");
        msg.setForeground(Color.darkGray);
        msgPanel.setLayout(new BorderLayout());
        msgPanel.add(msg,BorderLayout.CENTER);
        return msgPanel;
    }

    private static AfPanel createUserMsg(String[] subMsg){
        AfPanel msgPanel = new AfPanel();
        msgPanel.padding(5);
        msgPanel.setLayout(new BorderLayout());
        AfPanel namePanel = new AfPanel();
        AfPanel mPanel = new AfPanel();
        msgPanel.add(namePanel,BorderLayout.PAGE_START);
        msgPanel.add(mPanel,BorderLayout.PAGE_END);
        namePanel.setLayout(new BorderLayout());
        JLabel nameLabel = new JLabel(subMsg[0]);
        nameLabel.setForeground(new Color(160,160,160));
        namePanel.add(nameLabel, BorderLayout.WEST);
        mPanel.setLayout(new AfXLayout());
        JLabel msgTextArea = new JLabel(subMsg[1]);
        msgTextArea.setBackground(new Color(243,243,243));
        msgTextArea.setBorder(new CompoundBorder(msgTextArea.getBorder(),new EmptyBorder(0,6,0,6)));
        msgTextArea.setOpaque(true);
        mPanel.add(msgTextArea,msgTextArea.getPreferredSize().width+"px");
        return msgPanel;
    }

    private static int countNumberOfNewLine(String msg){
        int len = msg.length();
        String afterMsg = msg.replaceAll("\n","");
        return len-afterMsg.length()==0 ? 1:len-afterMsg.length();
    }

    private static class CenterLayout extends AfSimpleLayout {
        private Container target;

        public CenterLayout(Container target){
            this.target = target;
        }

        @Override
        public void layoutContainer(Container parent) {
            int w = parent.getWidth();
            int h = parent.getHeight();
            if (target.isVisible()){
                Dimension size = target.getPreferredSize();
                int x = (w-size.width)/2;
                int y = (h-size.height)/2;
                target.setBounds(x,y,size.width,size.height);
            }
        }
    }


}
