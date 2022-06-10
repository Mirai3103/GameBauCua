package client.view.component;


import client.event.EventHandler;
import client.utils.GlobalVariable;
import lombok.Getter;
import server.handler.HandleEvent;
import shared.model.event.ChatWorld;
import shared.model.event.EventPayload;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

@Getter
public class ChatPanel extends JPanel {
    private JTextArea  enteredText = new JTextArea(10, 32);
    private JTextField typedText   = new JTextField();
    private JButton    sendButton  = new JButton("Send");


    private EventHandler eventHandler;
    public void setHandleEvent(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }


    public ChatPanel() throws URISyntaxException, IOException {
        setLayout(new BorderLayout());
        typedText.setColumns(34);
        typedText.setFont(new Font("Arial", Font.PLAIN, 14));
        enteredText.setEditable(false);
        enteredText.setBackground(Color.LIGHT_GRAY);
        enteredText.setFont(new Font("Arial", Font.PLAIN, 10));

//        typedText.addActionListener(this);
        add(new JScrollPane(enteredText), BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(typedText);
        panel.add(sendButton);
        add(panel, BorderLayout.SOUTH);
        sendButton.addActionListener(e -> {
            try {
                EventPayload eventPayload = new EventPayload(EventPayload.EventType.CHAT_WORLD, new ChatWorld(typedText.getText()), GlobalVariable.userInfo);
                GlobalVariable.objectOutputStream.writeObject(eventPayload);
                typedText.setText("");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

}
