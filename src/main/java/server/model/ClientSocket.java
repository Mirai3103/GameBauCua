package server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


@NoArgsConstructor
@Data
public class ClientSocket {
    private User user;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    public ClientSocket(User user, Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.user = user;
        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

}
