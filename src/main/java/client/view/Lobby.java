package client.view;

import client.event.EventHandler;
import client.event.EventListener;
import client.utils.GlobalVariable;
import client.view.component.ChatPanel;
import lombok.Getter;
import server.handler.HandleEvent;
import server.model.User;
import shared.model.LoginReturned;
import shared.model.UserInfo;
import shared.model.event.CreateRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


@Getter

public class Lobby extends JFrame
{
    private UserInfo user;
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

    public void init(){
        addWindowListener(GlobalVariable.windowAdapter);
        addWindowListener(GlobalVariable.windowAdapter);

        this.eventHandler = GlobalVariable.eventHandler;
        eventHandler.setCurrentFrame(this);
        EventListener eventListener = new EventListener();
        eventListener.start();
        eventListener.addListener(eventHandler);

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        setSize(500, 600);
        setResizable(false);
        setLayout(new GridLayout(3, 1));
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1,2));
        JLabel avt = new JLabel("avt");
        mainPanel.add(avt);
        JPanel info = new JPanel(new GridLayout(2,2));
        JLabel hovaten = new JLabel("Full name: ");
        JLabel moneyLB = new JLabel("Money: ");
        JLabel hvt = new JLabel(GlobalVariable.userInfo.getFullName());
        JLabel mn = new JLabel(GlobalVariable.userInfo.getMoney()+"$");
        info.add(hovaten);info.add(hvt);
        info.add(moneyLB);
        info.add(mn);
        mainPanel.add(info);
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
        button2.addActionListener(e->{
            boolean isHasPassword = JOptionPane.showConfirmDialog(this, "Bạn có muốn tạo mật khẩu không?", "Mật khẩu", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            String password = "";
            if (isHasPassword) {
                password = JOptionPane.showInputDialog(this, "Nhập mật khẩu");
            }
            int maxPlayer = Integer.parseInt(JOptionPane.showInputDialog(this, "Nhập số người tối đa cho phòng"));
            CreateRoom createRoom = new CreateRoom( password, maxPlayer);
            try {
                eventHandler.createRoom(createRoom);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        button1.addActionListener(e->{
            try {
                eventHandler.fastJoinRoom();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        button3.addActionListener(e->{
            try {
                eventHandler.getListAvailableRoom();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        setVisible(true);
    }
    public Lobby(LoginReturned loginReturned, EventHandler eventHandler){
        super("Lobby");

        GlobalVariable.eventHandler = eventHandler;
        this.user = loginReturned.getUserInfo();
        this.eventHandler = eventHandler;
        init();
        GlobalVariable.lobbyFrame = this;

    }

}
