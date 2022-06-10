package client.utils;


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
    public static boolean isRoomOwner(){
        return userInfo.getId().equals(currentRoom.getRoomOwner().getId());
    }

}
