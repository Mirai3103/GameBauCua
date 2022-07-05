package client.service;


import client.utils.GlobalVariable;

import shared.model.LoginReturned;


import javax.swing.*;

import java.io.*;
import shared.model.LoginPayload;
import shared.model.event.EventPayload;
import shared.model.event.RegisterPayload;
import shared.model.event.RegisterResponse;

public class AuthService {
    private final JFrame parentFrame;

    public AuthService(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public LoginReturned Login(LoginPayload loginPayLoad) throws IOException, ClassNotFoundException {


        EventPayload eventPayload = new EventPayload(EventPayload.EventType.LOGIN_REQ, loginPayLoad,null);
        GlobalVariable.objectOutputStream.writeObject(eventPayload);
        GlobalVariable.objectOutputStream.flush();

        LoginReturned loginReturned = (LoginReturned)  GlobalVariable.objectInputStream.readObject();
        if (loginReturned.getUserInfo() == null) {
            JOptionPane.showMessageDialog(null, "wrong username or password", "Error", JOptionPane.ERROR_MESSAGE);

        }else {
            return loginReturned;
        }
        return null;
    }

    public RegisterResponse registerResponse(RegisterPayload registerPayload) throws IOException, ClassNotFoundException {
        EventPayload eventPayload = new EventPayload(EventPayload.EventType.REGISTER, registerPayload,null);
        GlobalVariable.objectOutputStream.writeObject(eventPayload);
        GlobalVariable.objectOutputStream.flush();
       EventPayload eventPayload1 = (EventPayload) GlobalVariable.objectInputStream.readObject();

        return (RegisterResponse) eventPayload1.getEventData();
    }
}