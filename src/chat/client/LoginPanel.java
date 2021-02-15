package chat.client;

import chat.client.swing.AfPanel;
import chat.client.swing.layout.AfSimpleLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends AfPanel {
    private AfPanel loginArea;
    private JLabel userName,roomPW;
    private JTextField inputUserName,inputRoomPW;
    private JButton okButton;
    private String uName;
    private Display display;


    public LoginPanel(Display display){
        this.display = display;
        loginArea = new AfPanel();
        userName = new JLabel("名前:");
        roomPW = new JLabel("ルーム番号:");
        inputUserName = new JTextField(10);
        inputRoomPW = new JTextField(10);
        okButton = new JButton("確認");
        loginArea.add(userName);
        loginArea.add(inputUserName);
        loginArea.add(roomPW);
        loginArea.add(inputRoomPW);
        loginArea.add(okButton);
        setLayout(new LoginPageLayout());
        add(loginArea);

        okButton.addActionListener(e -> okButtonClicked());
    }

    private void okButtonClicked(){
        String pw = inputRoomPW.getText();
        if (!pw.equals(AppInfo.password)){
            JOptionPane.showMessageDialog(this,"正しいルーム番号を入力してください。");
        }else {
            String name = inputUserName.getText();
            if (name.equals("")){
                JOptionPane.showMessageDialog(this,"名前を入力してください。");
            }else {
                uName = name;
                display.getClient().setName(uName);
                display.getClient().startLink();
                new Thread(new Receive(display.getClient(),display)).start();
                display.getCardLayout().show(display.getRoot(),"1");
                display.initOnlineUsersBar();
            }
        }
    }

    public String getuName() {
        return uName;
    }

    private class LoginPageLayout extends AfSimpleLayout {
        @Override
        public void layoutContainer(Container parent) {
            int w = parent.getWidth();
            int h = parent.getHeight();
            if (loginArea.isVisible()){
                Dimension size = loginArea.getPreferredSize();
                int x = (w-size.width)/2;
                int y = (h-size.height)/2;
                loginArea.setBounds(x,y,size.width,size.height);
            }
        }
    }

}
