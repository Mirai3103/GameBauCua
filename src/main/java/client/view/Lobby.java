package client.view;

import client.event.EventHandler;
import client.event.EventListener;
import client.view.component.ChatPanel;
import server.handler.HandleEvent;
import server.model.User;
import shared.model.LoginReturned;
import shared.model.UserInfo;

import javax.swing.*;
import java.awt.*;


public class Lobby extends JFrame
{
    private UserInfo user;
    private String token;
    private JPanel panel1;
    private JPanel mainPanel;
    private ChatPanel chatPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private EventHandler eventHandler;
    public Lobby(LoginReturned loginReturned, EventHandler eventHandler){
        super("sảnh chờ");
        this.user = loginReturned.getUserInfo();
        this.token = loginReturned.getToken();
        this.eventHandler = eventHandler;
        EventListener eventListener = new EventListener(eventHandler.getObjectInputStream());
        eventListener.start();
        eventListener.addListener(eventHandler);

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        setSize(500, 600);
        setResizable(false);
        setLayout(new GridLayout(3, 1));
        mainPanel = new JPanel();
        try {
            chatPanel = new ChatPanel();
            chatPanel.setHandleEvent(eventHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(mainPanel);
        add(chatPanel);

        panel1 = new JPanel();

        panel1.setLayout(new GridLayout(3, 2));
        button1 = new JButton("Vào nhanh");
        button2 = new JButton("Tạo phòng");
        button3 = new JButton("Tìm phòng");
        button4 = new JButton("xếp hạng");
        button5 = new JButton("Đăng xuất");
        button6 = new JButton("Thoát");
        button1.setFont(buttonFont);
        button2.setFont(buttonFont);
        button3.setFont(buttonFont);
        button4.setFont(buttonFont);
        button5.setFont(buttonFont);
        button6.setFont(buttonFont);
        panel1.add(button1);
        panel1.add(button2);
        panel1.add(button3);
        panel1.add(button4);
        panel1.add(button5);
        panel1.add(button6);
        add(panel1, BorderLayout.SOUTH);

    setVisible(true);

    }

}
