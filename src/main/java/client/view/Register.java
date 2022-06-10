package client.view;

import client.model.RegisterPayLoad;
import client.service.AuthService;
import server.model.User;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register extends JFrame    {
    JLabel l1, l2, l3, l4, l5;  //all labels for textField
    JTextField tf1, tf2;   // others fields
    JButton btn1, btn2;  //buttons for signup and clear
    JPasswordField p1, p2;  // password fields

    int ln;

    Register()
    {
        setVisible(true);
        setSize(700, 700);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Registration Form ");
        l1 = new JLabel("Registration Form :");
        l1.setForeground(Color.blue);
        l1.setFont(new Font("Serif", Font.BOLD, 20));
        l2 = new JLabel("Full name:");
        l3 = new JLabel("username:");
        l4 = new JLabel("Create Password:");
        l5 = new JLabel("Confirm Password:");

        tf1 = new JTextField();
        tf2 = new JTextField();
        p1 = new JPasswordField();
        p2 = new JPasswordField();
        btn1 = new JButton("Submit");
        btn2 = new JButton("Clear");

//        btn2.addActionListener(this);
        l1.setBounds(100, 30, 400, 30);
        l2.setBounds(80, 70, 200, 30);
        l3.setBounds(80, 110, 200, 30);
        l4.setBounds(80, 150, 200, 30);
        l5.setBounds(80, 190, 200, 30);

        tf1.setBounds(300, 70, 200, 30);
        tf2.setBounds(300, 110, 200, 30);
        p1.setBounds(300, 150, 200, 30);
        p2.setBounds(300, 190, 200, 30);

        btn1.setBounds(100, 250, 100, 30);
        btn2.setBounds(230, 250, 100, 30);
        add(l1);
        add(l2);
        add(tf1);
        add(l3);
        add(tf2);
        add(l4);
        add(p1);
        add(l5);
        add(p2);
        add(btn1);
        add(btn2);
        setResizable(false);
    }

    public static void main(String args[])
    {
        new Register();
    }

}

