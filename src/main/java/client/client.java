package client;

import client.utils.GlobalVariable;
import client.view.Login;

import java.awt.*;

public class client {
    public static void run() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GlobalVariable.currentFrame = new Login();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
