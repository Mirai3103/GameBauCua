package client.utils;


import client.event.EventHandler;
import shared.model.Room;
import shared.model.UserInfo;

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

    public static boolean isRoomOwner(){
        return userInfo.getId().equals(currentRoom.getRoomOwner().getId());
    }
    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

}
