package client.utils;


import client.event.EventHandler;
import shared.model.Room;
import shared.model.UserInfo;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GlobalVariable {
    public static Socket socket;
    public static ObjectInputStream objectInputStream;
    public static ObjectOutputStream objectOutputStream;
    public static UserInfo userInfo;
    public static Room currentRoom;
    public static EventHandler eventHandler;
    public static JFrame currentFrame;
    public static WindowAdapter windowAdapter=new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            try {
                GlobalVariable.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    };   public static boolean isRoomOwner(){
        return userInfo.getId().equals(currentRoom.getRoomOwner().getId());
    }
    public static void setTimeout(Runnable callback, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                callback.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

}
