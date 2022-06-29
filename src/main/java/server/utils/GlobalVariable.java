package server.utils;

import org.slf4j.Logger;
import server.model.RoomResult;
import shared.model.Room;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable {
    public static List<Room> rooms = new ArrayList<>();
    public static int sequenceId = 0;
    public  static  List<RoomResult> roomResults = new ArrayList<>();

}
