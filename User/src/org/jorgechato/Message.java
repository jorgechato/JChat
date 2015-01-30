package org.jorgechato;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jorge on 30/01/15.
 */
public class Message {
    private JPanel panel1;
    private JLabel nick;
    private JLabel message;

    public Message(String color, String nick, String message){
        this.nick.setForeground(Color.decode("#" + color));
        this.nick.setText(nick);
        this.message.setText(message);
    }

    public JLabel getNick() {
        return nick;
    }

    public JLabel getMessage() {
        return message;
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
