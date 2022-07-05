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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter

public class RoomForPlayer extends JFrame {
    private JButton bauButton;
    private JButton cuaButton;
    private JButton tomButton;
    private JButton caButton;
    private JButton gaButton;
    private JButton naiButton;
    private List<JButton> mucCuoc = new ArrayList<>();
    private DiaForPlayer diaForPlayer;
    private JPanel panel1;
    private JPanel mainPanel;
    public GameState.State gameState;
    private JLabel stateLabel;
    private int muccuoctien = 500;
    private int cuaBet = 0;
    private int bauBet = 0;
    private int tomBet = 0;
    private int caBet = 0;
    private int gaBet = 0;
    private int naiBet = 0;
    private int bet = 0;
    private BetList betList;
    private JLabel timeLB;
    private JLabel moneyLB;
    private JButton clearButton;
    private JLabel playerLB;
    @Setter
    private String[] result;

    public RoomForPlayer() throws IOException {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    GlobalVariable.eventHandler.leaveRoom();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JPanel muccuocpanel = new JPanel();
        muccuocpanel.setLayout(new GridLayout(1,4));
        for (int i = 500; i <= 2000; i+=500) {
           JButton t = new JButton(i+ "$");
            mucCuoc.add(t);
           muccuocpanel.add(t);
        }
        for (JButton t: mucCuoc
             ) {
            t.addActionListener(e->{

                muccuoctien = Integer.parseInt(t.getText().replace("$",""));
                for (JButton bt: mucCuoc
                ) {
                    bt.setEnabled(true);
                }
                t.setEnabled(false);
            });
        }
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


        clearButton = new JButton("Clear");
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
                    bauBet += muccuoctien;
                    bauButton.setText("Bau " + bauBet + "$");
                    betList.addBet(new Bet(muccuoctien, Bet.BetType.BAU));
                }
                case "cua" -> {
                    bet++;
                    cuaBet += muccuoctien;
                    cuaButton.setText("Cua " + cuaBet + "$");
                    betList.addBet(new Bet(muccuoctien, Bet.BetType.CUA));
                }
                case "tom" -> {
                    bet++;
                    tomBet += muccuoctien;
                    tomButton.setText("Tom " + tomBet + "$");
                    betList.addBet(new Bet(muccuoctien, Bet.BetType.TOM));
                }
                case "ca" -> {
                    bet++;
                    caBet += muccuoctien;
                    caButton.setText("Ca " + caBet + "$");
                    betList.addBet(new Bet(muccuoctien, Bet.BetType.CA));
                }
                case "ga" -> {
                    bet++;
                    gaBet += muccuoctien;
                    gaButton.setText("Ga " + gaBet + "$");
                    betList.addBet(new Bet(muccuoctien, Bet.BetType.GA));
                }
                case "nai" -> {
                    bet++;
                    naiBet += muccuoctien;
                    naiButton.setText("Nai " + naiBet + "$");
                    betList.addBet(new Bet(muccuoctien, Bet.BetType.NAI));
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
        timeLB = new JLabel("60s");


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
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(stateLabel);
        topPanel.add(timeLB);
        JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        moneyLB = new JLabel(GlobalVariable.userInfo.getMoney()+ "$");
        playerLB = new JLabel(""+GlobalVariable.currentRoom.getRoomPlayers().size()+"/"+GlobalVariable.currentRoom.getRoomMaxPlayer());

        leftPanel.add(moneyLB);
        leftPanel.add(new JLabel());
        leftPanel.add(playerLB);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        JPanel southpanel = new JPanel();
        southpanel.setLayout(new GridLayout(2,1));
        southpanel.add(muccuocpanel);
        southpanel.add(clearButton);
        mainPanel.add(southpanel, BorderLayout.SOUTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);



        add(mainPanel);
        add(panel1);
        setVisible(true);
        setGameState(GameState.State.WAITING);
    }

    public void reDrawButton() {
        bauButton.setText("Bau " + bauBet + "$");
        cuaButton.setText("Cua " + cuaBet + "$");
        tomButton.setText("Tom " + tomBet + "$");
        caButton.setText("Ca " + caBet + "$");
        gaButton.setText("Ga " + gaBet + "$");
        naiButton.setText("Nai " + naiBet + "$");
    }
    public void updateMoney(){
        moneyLB.setText(GlobalVariable.userInfo.getMoney()+ "$");
    }

    public void setGameState(GameState.State state) throws IOException {
        this.gameState = state;
        stateLabel.setText(state.toString());
        System.out.println(GlobalVariable.userInfo);

        switch (state) {
            case END -> {
                bauButton.setEnabled(false);
                cuaButton.setEnabled(false);
                tomButton.setEnabled(false);
                caButton.setEnabled(false);
                gaButton.setEnabled(false);
                naiButton.setEnabled(false);
                clearButton.setEnabled(false);
                System.out.println(betList);

                System.out.println("end game:");
                EventPayload eventPayload = new EventPayload();
                eventPayload.setEventType(EventPayload.EventType.END_GAME);
                eventPayload.setSender(GlobalVariable.userInfo);
                System.out.println(GlobalVariable.userInfo);
                eventPayload.setEventData(betList);
                GlobalVariable.objectOutputStream.writeObject(eventPayload);

                GlobalVariable.setTimeout(() -> {
                    try {
                        diaForPlayer.drawKQ(result);
                        diaForPlayer.repaint();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, 1000 * 1);
            }
            case WAITING -> {
                bauButton.setEnabled(false);
                cuaButton.setEnabled(false);
                tomButton.setEnabled(false);
                caButton.setEnabled(false);
                gaButton.setEnabled(false);
                naiButton.setEnabled(false);
                clearButton.setEnabled(false);
                diaForPlayer.clearDia();
            }
            case PLAYING -> {
                bauButton.setEnabled(true);
                cuaButton.setEnabled(true);
                tomButton.setEnabled(true);
                caButton.setEnabled(true);
                gaButton.setEnabled(true);
                clearButton.setEnabled(true);
                naiButton.setEnabled(true);
                diaForPlayer.clearDia();
                Thread timer = new Thread(() -> {
                    int time = 61;
                    while (time != -1) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        timeLB.setText("Time: " + time + "S");
                        time--;
                    }
                    try {
                        this.setGameState(GameState.State.END);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                timer.start();
            }
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
        bet = 0 ;
        bauBet = 0;
        cuaBet = 0;
        tomBet = 0;
        caBet = 0;
        gaBet = 0;
        naiBet = 0;
        betList.setBets(new ArrayList<>());
        bauButton.setEnabled(true);
        cuaButton.setEnabled(true);
        tomButton.setEnabled(true);
        caButton.setEnabled(true);
        gaButton.setEnabled(true);
        naiButton.setEnabled(true);
        bauButton.setText("bau");
        cuaButton.setText("cua");
        tomButton.setText("tom");
        caButton.setText("ca");
        gaButton.setText("ga");
        naiButton.setText("nai");
    }

    public static void main(String[] args) throws IOException {
        RoomForPlayer roomForOwner = new RoomForPlayer();
        roomForOwner.setVisible(true);
    }

}

