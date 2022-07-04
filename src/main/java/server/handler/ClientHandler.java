package server.handler;

import server.model.ClientSocket;
import shared.model.event.EventPayload;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ClientHandler extends Thread{
    private ClientSocket clientSocket;
    private HandleEvent handleEvent;
    private ObjectInputStream objectInputStream;

    public ClientHandler(ClientSocket clientSocket, HandleEvent handleEvent) throws IOException {
        this.clientSocket = clientSocket;
        this.handleEvent = handleEvent;
        this.objectInputStream = clientSocket.getObjectInputStream();

    }

    @Override
    public void run() {
        EventPayload event;
        while (true) {
            try {
                event = (EventPayload) objectInputStream.readObject();
                handleEvent.handle(event, clientSocket);
            } catch (IOException | ClassNotFoundException e) {
                if(e instanceof SocketException) {
                    System.out.println("Client disconnected");
                    try {
                        clientSocket.getSocket().close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }else {
                    throw new RuntimeException(e);
                }


            }
        }
    }


}
