package org.jorgechato;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by jorge on 28/01/15.
 */
public class Window implements ActionListener{
    private JScrollPane contacts;
    private JPanel chat;
    private JPanel panel1;
    private JPanel contactsRow;
    private JButton send;
    private JTextField textField1;
    private JPanel chatArea;
    private Contact contact;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Window");
        frame.setContentPane(new Window().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Window(){
        send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        send.addActionListener(this);

        for (int i = 0 ; i<10;i++) {
            contactsRow.add(new Contact().getPanel1());
        }
        contactsRow.updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
    }
}
