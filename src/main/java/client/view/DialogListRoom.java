package client.view;

import shared.model.event.AvailRoom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class DialogListRoom extends JDialog {
    private String data;
    private JTable table1;
    private JButton okButton;
    private JButton cancelButton;
    public DialogListRoom(JFrame parent, AvailRoom availRoom) {
        super(parent, "List Room", true);
        setSize(500, 600);
        setResizable(false);
        setLayout(new BorderLayout());

        String clm[] = {"Room ID", "Room Name", "isHasPassword", "isReady"};

        DefaultTableModel model = new DefaultTableModel(clm, 0);
        for (int i = 0; i < availRoom.getRooms().size(); i++) {
            String[] row = {availRoom.getRooms().get(i).getRoomId()+"", availRoom.getRooms().get(i).getRoomName(), availRoom.getRooms().get(i).getRoomPassword().equalsIgnoreCase("")?"Khong":"co", availRoom.getRooms().get(i).getRoomState().toString()};
            model.addRow(row);
        }
        table1 = new JTable(model);
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        JScrollPane scrollPane = new JScrollPane(table1);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        okButton.addActionListener(e->{
            int row = table1.getSelectedRow();
            if (row != -1) {
                data = table1.getValueAt(row, 0).toString();
                dispose();
            }
        });

    }
    public String run() {
        setVisible(true);
        return data;
    }
}
