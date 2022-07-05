package server.handler;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import server.model.ClientSocket;
import server.utils.HibernateUtils;
import shared.model.LoginReturned;
import server.model.User;
import shared.model.UserInfo;
import shared.model.event.EventPayload;
import shared.model.LoginPayload;
import shared.model.event.RegisterPayload;
import shared.model.event.RegisterResponse;

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
    public static Logger logger = org.slf4j.LoggerFactory.getLogger(LoginState.class);

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

        EventPayload eventPayload = null;
        try {
            while (true) {
                try {
                    eventPayload = (EventPayload) objectInputStream.readObject();
                }catch (ClassCastException e) {
                    socket.close();
                    logger.info("Client disconnected");
                    return;
                }

                switch (eventPayload.getEventType()){
                    case LOGIN_REQ -> {
                        LoginPayload loginPayLoad = (LoginPayload) eventPayload.getEventData();
                        Session session = HibernateUtils.getSessionFactory().openSession();
                        User user = session.createQuery("from User where username = :username and password =:password", User.class)
                                .setParameter("username", loginPayLoad.getUsername())
                                .setParameter("password", loginPayLoad.getPassword())
                                .uniqueResult();
                        session.close();
                        if (user != null) {
                            logger.info("User " + loginPayLoad.getUsername() + " logged in");
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
                            return;
                        } else {
                            logger.error("A user login failed");
                            LoginReturned loginReturned = new LoginReturned(null);
                            objectOutputStream.writeObject(loginReturned);
                        }

                    }
                    case REGISTER -> {
                        System.out.println("Registering");
                        Session session = HibernateUtils.getSessionFactory().openSession();
                        session.beginTransaction();
                        RegisterPayload registerPayload = (RegisterPayload) eventPayload.getEventData();
                        int userOld = session.createQuery("select a from User a where a.username= :i").setParameter("i",registerPayload.getUserName()).getResultList().size();
                        EventPayload eventPayload1 = new EventPayload();
                        eventPayload1.setEventType(EventPayload.EventType.REGISTER_RESPONSE);
                        if (userOld != 0){
                            eventPayload1.setEventData(new RegisterResponse("username đã tồn tại",false));
                        }else {
                            User user = new User();
                            user.setMoney(10000L);
                            user.setUsername(registerPayload.getUserName());
                            user.setFullName(registerPayload.getFullName());
                            user.setPassword(registerPayload.getPassword());
                            session.save(user);
                            session.getTransaction().commit();
                            eventPayload1.setEventData(new RegisterResponse("Đăng ký thành công, mời bạn đăng nhập",true));

                        }
                        objectOutputStream.writeObject(eventPayload1);
                    }

                }




            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
