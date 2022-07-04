package client.view;

import shared.model.Ranks;
import shared.model.event.AvailRoom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class DialogRank extends JDialog {
    private String data;
    private JTable table1;
    private JButton cancelButton;
    public DialogRank(JFrame parent, Ranks availRoom) {
        super(parent, "List Room", true);
        setSize(500, 600);
        setResizable(false);
        setLayout(new BorderLayout());

        String clm[] = {"Số thứ tự", "Tên người chơi", "Số tiền"};

        DefaultTableModel model = new DefaultTableModel(clm, 0);
        for (int i = 0; i < availRoom.getRanks().size(); i++) {
            String[] row = {i+1+"", availRoom.getRanks().get(i).getFullName(), availRoom.getRanks().get(i).getMoney()+"$"};
            model.addRow(row);
        }
        table1 = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table1);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        add(buttonPanel, BorderLayout.SOUTH);
    }
    public String run() {
        setVisible(true);
        return data;
    }
}
