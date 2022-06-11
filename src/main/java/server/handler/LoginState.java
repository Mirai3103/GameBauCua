package server.handler;

import client.model.LoginPayLoad;
import com.auth0.jwt.JWT;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import server.model.ClientSocket;
import server.utils.HibernateUtils;
import shared.model.LoginReturned;
import server.model.User;
import shared.model.UserInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
public class LoginState extends Thread {
    private Socket socket;
    private HandleEvent handleEvent;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public LoginState(Socket socket, HandleEvent handleEvent) {
        this.socket = socket;
        this.handleEvent = handleEvent;

        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        LoginPayLoad loginPayLoad;
        try {
            while (true) {
                loginPayLoad = (LoginPayLoad) objectInputStream.readObject();
                System.out.println("LoginPayLoad: " + loginPayLoad);

                Session session = HibernateUtils.getSessionFactory().openSession();
                User user = session.createQuery("from User where username = :username and password =:password", User.class)
                        .setParameter("username", loginPayLoad.getUsername())
                        .setParameter("password", loginPayLoad.getPassword())
                        .uniqueResult();
                session.close();
                if (user != null) {
                    System.out.println("LoginReturned: " + user);
                    ClientSocket clientSocket = new ClientSocket(new UserInfo(user), socket, objectInputStream, objectOutputStream);
                    UserInfo userInfo =new UserInfo(user);
                    LoginReturned loginReturned = new LoginReturned( userInfo);
                    System.out.println("LoginReturned: " + loginReturned);
                    objectOutputStream.writeObject(loginReturned);
                    handleEvent.getClientSockets().add(clientSocket);
                    ClientHandler clientHandler = new ClientHandler(clientSocket, handleEvent);
                    clientHandler.start();
                    this.join();
                    break;
                } else {
                    LoginReturned loginReturned = new LoginReturned(null);
                    objectOutputStream.writeObject(loginReturned);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
