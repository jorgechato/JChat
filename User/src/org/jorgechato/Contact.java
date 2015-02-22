package org.jorgechato;

import com.bolivia.label.CLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by jorge on 28/01/15.
 */
public class Contact implements MouseListener{
    private JPanel panel1;
    private CLabel word;
    private JLabel name;
    private CLabel connected;
    private JLabel typing;
    public static final String GREEN = "7dcd40";
    public static final String RED = "ff4842";
    private String nick,color;
    private boolean blacklist;

    public Contact(String nick, String color){
        this.nick = nick;
        this.color = color;
        word.setBackground(Color.decode(color));
        connected.setBackground(Color.decode("#" + GREEN));

        name.setText(nick);
        word.setText(String.valueOf(nick.charAt(0)).toUpperCase());
        typing.setVisible(false);

        panel1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        connected.addMouseListener(this);
        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }

    public JLabel getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JLabel getTyping() {
        return typing;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (!blacklist){
            connected.setBackground(Color.decode("#" + RED));
            Window.addToBlackList(this);
            blacklist = true;
        }else {
            connected.setBackground(Color.decode("#" + GREEN));
            Window.removeFromBlackList(this);
            blacklist = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
