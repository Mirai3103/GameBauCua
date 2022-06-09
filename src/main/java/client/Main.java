package client;

import client.view.Login;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
