package client.view;

import client.utils.GlobalVariable;
import client.view.component.DiaForPlayer;
import client.view.component.IHaveNoIdea;
import lombok.Getter;
import lombok.Setter;
import shared.model.event.Bet;
import shared.model.event.BetList;
import shared.model.event.EventPayload;
import shared.model.event.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;

@Getter

public class RoomForPlayer extends JFrame {
    private JButton bauButton;
    private JButton cuaButton;
    private JButton tomButton;
    private JButton caButton;
    private JButton gaButton;
    private JButton naiButton;
    private DiaForPlayer diaForPlayer;
    private JPanel panel1;
    private JPanel mainPanel;
    public GameState.State gameState;
    private JLabel stateLabel;
    private int cuaBet = 0;
    private int bauBet = 0;
    private int tomBet = 0;
    private int caBet = 0;
    private int gaBet = 0;
    private int naiBet = 0;
    private int bet = 0;
    private BetList betList;
    @Setter
    private String[] result;

    public RoomForPlayer() {
        GlobalVariable.eventHandler.setCurrentFrame(this);
        betList = new BetList();
        setBackground(Color.DARK_GRAY);
        stateLabel = new JLabel();
        bauButton = new JButton("Bau");
        cuaButton = new JButton("Cua");
        tomButton = new JButton("Tom");
        caButton = new JButton("Ca");
        gaButton = new JButton("Ga");
        naiButton = new JButton("Nai");

        Button clearButton = new Button("Clear");
        clearButton.addActionListener(e -> {
            this.clearBet();
        });

        setLayout(new GridLayout(2, 1));
        setSize(500, 600);
        setResizable(false);
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        panel1 = new JPanel();
        diaForPlayer = new DiaForPlayer("src/main/resources/images/caidiatrong.png");


        ActionListener actionListener = e -> {
            switch (((JButton) e.getSource()).getText().toLowerCase(Locale.ROOT)) {
                case "bau" -> {
                    bet++;
                    bauBet += 500;
                    bauButton.setText("Bau " + bauBet + "$");
                    betList.addBet(new Bet(500, Bet.BetType.BAU));
                }
                case "cua" -> {
                    bet++;
                    cuaBet += 500;
                    cuaButton.setText("Cua " + cuaBet + "$");
                    betList.addBet(new Bet(500, Bet.BetType.BAU));
                }
                case "tom" -> {
                    bet++;
                    tomBet += 500;
                    tomButton.setText("Tom " + tomBet + "$");
                    betList.addBet(new Bet(500, Bet.BetType.BAU));
                }
                case "ca" -> {
                    bet++;
                    caBet += 500;
                    caButton.setText("Ca " + caBet + "$");
                    betList.addBet(new Bet(500, Bet.BetType.BAU));
                }
                case "ga" -> {
                    bet++;
                    gaBet += 500;
                    gaButton.setText("Ga " + gaBet + "$");
                    betList.addBet(new Bet(500, Bet.BetType.BAU));
                }
                case "nai" -> {
                    bet++;
                    naiBet += 500;
                    naiButton.setText("Nai " + naiBet + "$");
                    betList.addBet(new Bet(500, Bet.BetType.BAU));
                }
            }
            if (bet == 3) {
                bauButton.setEnabled(false);
                cuaButton.setEnabled(false);
                tomButton.setEnabled(false);
                caButton.setEnabled(false);
                gaButton.setEnabled(false);
                naiButton.setEnabled(false);
            }
        };

        bauButton.addActionListener(actionListener);
        cuaButton.addActionListener(actionListener);
        tomButton.addActionListener(actionListener);
        caButton.addActionListener(actionListener);
        gaButton.addActionListener(actionListener);
        naiButton.addActionListener(actionListener);


        panel1.setLayout(new GridLayout(2, 3));
        panel1.add(bauButton);
        panel1.add(cuaButton);
        panel1.add(tomButton);
        panel1.add(caButton);
        panel1.add(gaButton);
        panel1.add(naiButton);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(diaForPlayer, BorderLayout.CENTER);
        mainPanel.add(stateLabel, BorderLayout.NORTH);
        mainPanel.add(clearButton, BorderLayout.SOUTH);

        add(mainPanel);
        add(panel1);
        setVisible(true);
    }

    public void reDrawButton() {
        bauButton.setText("Bau " + bauBet + "$");
        cuaButton.setText("Cua " + cuaBet + "$");
        tomButton.setText("Tom " + tomBet + "$");
        caButton.setText("Ca " + caBet + "$");
        gaButton.setText("Ga " + gaBet + "$");
        naiButton.setText("Nai " + naiBet + "$");
    }


    public void setGameState(GameState.State state) throws IOException {
        this.gameState = state;
        stateLabel.setText(state.toString());


        switch (state) {
            case END: {
                EventPayload eventPayload = new EventPayload();
                eventPayload.setEventType(EventPayload.EventType.BET);
                eventPayload.setSender(GlobalVariable.userInfo);
                eventPayload.setEventData(betList);
                GlobalVariable.objectOutputStream.writeObject(eventPayload);

                GlobalVariable.setTimeout(() -> {
                    try {
                        diaForPlayer.drawKQ(result);
                        diaForPlayer.repaint();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, 1000*10);
                break;
            }

            case WAITING:
                bauButton.setEnabled(false);
                cuaButton.setEnabled(false);
                tomButton.setEnabled(false);
                caButton.setEnabled(false);
                gaButton.setEnabled(false);
                naiButton.setEnabled(false);
                diaForPlayer.clearDia();

                break;


            case PLAYING:
                bauButton.setEnabled(true);
                cuaButton.setEnabled(true);
                tomButton.setEnabled(true);
                caButton.setEnabled(true);
                gaButton.setEnabled(true);
                naiButton.setEnabled(true);
                diaForPlayer.clearDia();
                GlobalVariable.setTimeout(() -> {
                    try {
                       this.setGameState(GameState.State.END);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, 30*1000);
                break;
        }
    }


    public void plusBauBet(int bet) {
        bauBet += bet;
    }

    public void plusCuaBet(int bet) {
        cuaBet += bet;
    }

    public void plusTomBet(int bet) {
        tomBet += bet;
    }

    public void plusCaBet(int bet) {
        caBet += bet;
    }

    public void plusGaBet(int bet) {
        gaBet += bet;
    }

    public void plusNaiBet(int bet) {
        naiBet += bet;
    }

    public void clearBet() {
        bauBet = 0;
        cuaBet = 0;
        tomBet = 0;
        caBet = 0;
        gaBet = 0;
        naiBet = 0;
    }

    public static void main(String[] args) {
        RoomForPlayer roomForOwner = new RoomForPlayer();
        roomForOwner.setVisible(true);
    }

}

