package org.jorgechato;

import com.bolivia.label.CLabel;

import javax.swing.*;
import java.awt.*;
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
    private boolean con;
    private String nick,color;

    public Contact(String nick,String color){
        this.nick = nick;
        this.color = color;
        word.setBackground(Color.decode(color));
        connected.setBackground(Color.decode("#" + GREEN));

        name.setText(nick);
        word.setText(String.valueOf(nick.charAt(0)).toUpperCase());

        panel1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel1.addMouseListener(this);
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
        if (con) {
            connected.setBackground(Color.decode("#" + RED ));
        }else {
            connected.setBackground(Color.decode("#" + GREEN));
        }con = !con;
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
