package org.jorgechato;

import com.bolivia.label.CLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

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
    private String [] color;
    private boolean con;
    private String nick;
    private String COLOR ;

    public Contact(String nick){
//        this.window = window;
        this.nick = nick;

        color = new String[20];
        color[0] = "F44336";
        color[1] = "E91E63";
        color[2] = "9C27B0";
        color[3] = "673AB7";
        color[4] = "3F51B5";
        color[5] = "2196F3";
        color[6] = "03A9F4";
        color[7] = "00BCD4";
        color[8] = "009688";
        color[9] = "4CAF50";
        color[10] = "8BC34A";
        color[11] = "CDDC39";
        color[12] = "FFEB3B";
        color[13] = "FFC107";
        color[14] = "FF9800";
        color[15] = "FF5722";
        color[16] = "795548";
        color[17] = "9E9E9E";
        color[18] = "607D8B";
        color[19] = "000000";

        Random ran = new Random();

        COLOR = "#" + color[ran.nextInt(20)];
        word.setBackground(Color.decode(COLOR));
        connected.setBackground(Color.decode("#" + GREEN));

        name.setText(nick);
        word.setText(String.valueOf(nick.charAt(0)).toUpperCase());

        panel1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel1.addMouseListener(this);
    }

    public String getCOLOR() {
        return COLOR;
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
