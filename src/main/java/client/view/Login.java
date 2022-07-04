package client.view;

import client.event.EventHandler;
import client.model.LoginPayLoad;
import client.service.AuthService;
import client.utils.GlobalVariable;
import client.view.Toast.ToastMessage;
import shared.model.LoginReturned;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private AuthService authService ;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JPanel contentPane;
    private JButton register;

    private void connectSocket(String host, int post) throws IOException {
        GlobalVariable.socket =  new Socket(host, post);
        GlobalVariable.objectOutputStream = new ObjectOutputStream(GlobalVariable.socket.getOutputStream());
        GlobalVariable.objectInputStream = new ObjectInputStream(GlobalVariable.socket.getInputStream());
        GlobalVariable.eventHandler = new EventHandler(this);
    }
    public Login(boolean isFirstTime) throws IOException {
        if(isFirstTime) {
            connectSocket("localhost", 8080);
        }
        Dimension screeDimension = Toolkit.getDefaultToolkit().getScreenSize();
        addWindowListener(GlobalVariable.windowAdapter);
        authService = new AuthService(this);
        System.out.println("Client connected");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(450, 190, 800, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(423, 13, 273, 93);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(481 -150, 170, 281, 68);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(481-150, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblUsername.setBounds(100, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPassword.setBounds(100, 286, 193, 52);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(300, 392, 162, 73);
        register = new JButton("Register");
        register.setFont(new Font("Tahoma", Font.PLAIN, 26));
        register.setBounds(500, 392, 162, 73);
        contentPane.add(btnNewButton);
        contentPane.add(register);
        register.addActionListener(e -> {
            this.dispose();
          GlobalVariable.currentFrame =   new Register();
        });
        btnNewButton.addActionListener(e -> {
            LoginPayLoad loginPayLoad = new LoginPayLoad(textField.getText(), passwordField.getText());
            try {
              LoginReturned loginReturned=  authService.Login(loginPayLoad);
              if(loginReturned!=null){
                    GlobalVariable.userInfo = loginReturned.getUserInfo();
                  this.dispose();
                  System.out.println("Login success");
                  GlobalVariable.currentFrame =    new Lobby(loginReturned, GlobalVariable.eventHandler);
              }

            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        setLocation((screeDimension.width-getWidth())/2,(screeDimension.height-getHeight())/2);


        setResizable(false);
        setVisible(true);


    }
}