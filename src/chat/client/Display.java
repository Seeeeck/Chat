package chat.client;

import chat.client.swing.AfPanel;
import chat.client.swing.layout.AfSimpleLayout;
import chat.client.swing.layout.AfXLayout;
import chat.client.swing.layout.AfYLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Display extends JFrame {
    private AfPanel root,mainPage,
            onlineUsersBar,chatArea,inputArea,showMsgArea;
    private LoginPanel loginPage;
    private CardLayout cardLayout;
    private JScrollPane showMsgJScrollPane,inputJScrollPane;
    private JTextArea inputText;
    private Client client;
    private JScrollBar sBar;
    private Dimension showMsgAreaSize;
    private int msgHeight;
    private Map<String ,JLabel> userNamesMap;

    public Display(Client client){
        this.client = client;
        userNamesMap = new HashMap<>();
        initBase();
        initShowMsgArea();
        initInputArea();
    }

    public Client getClient() {
        return client;
    }

    private void initBase() {
        msgHeight = 0;
        root = new AfPanel();
        loginPage = new LoginPanel(this);
        mainPage = new AfPanel();
        onlineUsersBar = new AfPanel();
        chatArea = new AfPanel();
        inputArea = new AfPanel();
        showMsgArea = new AfPanel();
        setContentPane(root);
        cardLayout = new CardLayout();
        root.setLayout(cardLayout);
        root.add("0",loginPage);
        root.add("1",mainPage);
        cardLayout.show(root,"0");
        mainPage.setLayout(new AfXLayout());
        mainPage.add(onlineUsersBar,"150px");
        mainPage.add(chatArea,"1w");
        chatArea.setLayout(new AfYLayout());
        root.padding(20);
    }

    private void initShowMsgArea(){
        showMsgJScrollPane = new JScrollPane(showMsgArea);
        showMsgArea.setLayout(new AfYLayout());
        showMsgArea.padding(4,1,0,5);
        chatArea.add(showMsgJScrollPane,"1w");
        showMsgAreaSize = new Dimension(showMsgArea.getPreferredSize());
        sBar = showMsgJScrollPane.getVerticalScrollBar();

    }

    private void initInputArea(){
        chatArea.add(inputArea,"150px");
        inputArea.setLayout(new BorderLayout());
        inputText = new JTextArea();
        inputJScrollPane = new JScrollPane(inputText);
        inputArea.add(inputJScrollPane,BorderLayout.CENTER);
        inputText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String msg = inputText.getText();
                    if(!msg.equals("")){
                        inputText.setText("");
                        client.send(msg.trim());
                        selfMsgToShowMsgArea(msg);

                    }
                }
            }
        });
    }

    public void initOnlineUsersBar(){
        onlineUsersBar.setLayout(new AfYLayout());
        JLabel title = new JLabel("メンバー");
        title.setFont(new Font(null,1,15));
        onlineUsersBar.add(title,"40px");
        JLabel selfName = new JLabel(client.getName());
        onlineUsersBar.add(selfName,"20px");
        userNamesMap.put(client.getName(),selfName);
        for(String name :client.getUsers()){
            JLabel userName = new JLabel(name);
            onlineUsersBar.add(userName,"20px");
            userNamesMap.put(name,userName);
        }
    }

    public void addNameToOnlineUsersBar(String name){
        JLabel user = new JLabel(name);
        onlineUsersBar.add(user,"20px");
        client.getUsers().add(name);
        userNamesMap.put(name,user);
    }

    public void removeNameToOnlineUsersBar(String name){
        userNamesMap.get(name).setVisible(false);
        client.getUsers().remove(name);
        userNamesMap.remove(name);
    }

    public void handleShowMsgArea(AfPanel msgPanel){
        Dimension msgSize = msgPanel.getPreferredSize();
        int height = msgHeight+msgSize.height;
        if (height >= showMsgAreaSize.height){
            showMsgArea.setPreferredSize(new Dimension(showMsgArea.getPreferredSize().width,height+2));
        }
        msgHeight = height+2;
        System.out.println("msgSize.height"+msgSize.height);
        System.out.println("msgSize.width"+msgSize.width);
        showMsgArea.add(msgPanel,msgSize.height+"px");
        revalidate();
        sBar.setValue(sBar.getMaximum()+msgSize.height);
    }

    private void selfMsgToShowMsgArea(String msg){
        AfPanel msgPanel = new AfPanel();
        msgPanel.padding(3,0,3,2);
        msgPanel.setLayout(new BorderLayout());
        JLabel selfMsg = new JLabel(msg);
        selfMsg.setOpaque(true);
        selfMsg.setBackground(new Color(222,243,253));
        selfMsg.setBorder(new CompoundBorder(selfMsg.getBorder(),new EmptyBorder(4,6,4,6)));
        msgPanel.add(selfMsg,BorderLayout.EAST);
        handleShowMsgArea(msgPanel);

    }

    public AfPanel getShowMsgArea() {
        return showMsgArea;
    }

    public AfPanel getRoot() {
        return root;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JScrollBar getsBar() {
        return sBar;
    }

    public Dimension getShowMsgAreaSize() {
        return showMsgAreaSize;
    }

    public int getMsgHeight() {
        return msgHeight;
    }

    public void setMsgHeight(int msgHeight) {
        this.msgHeight = msgHeight;
    }

}
