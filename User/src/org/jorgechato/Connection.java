package org.jorgechato;

import org.apache.commons.validator.routines.UrlValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by jorge on 28/01/15.
 */
public class Connection extends JDialog implements ActionListener{

    private JPanel panel1;
    private JTextField txthost;
    private JButton accept;
    private JButton cancel;
    private JTextField txtName;
    private static String host,nickname,color;
    private static boolean connectionAcepted;
    private Window window;
    private String [] colorVector;

    public Connection(Window window) {
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        this.window = window;

        init();
        color();
    }

    private void color() {
        colorVector = new String[20];
        colorVector[0] = "F44336";
        colorVector[1] = "E91E63";
        colorVector[2] = "9C27B0";
        colorVector[3] = "673AB7";
        colorVector[4] = "3F51B5";
        colorVector[5] = "2196F3";
        colorVector[6] = "03A9F4";
        colorVector[7] = "00BCD4";
        colorVector[8] = "009688";
        colorVector[9] = "4CAF50";
        colorVector[10] = "8BC34A";
        colorVector[11] = "CDDC39";
        colorVector[12] = "FFEB3B";
        colorVector[13] = "FFC107";
        colorVector[14] = "FF9800";
        colorVector[15] = "FF5722";
        colorVector[16] = "795548";
        colorVector[17] = "9E9E9E";
        colorVector[18] = "607D8B";
        colorVector[19] = "000000";
        Random ran = new Random();

        color = "#" + colorVector[ran.nextInt(20)];
    }

    private void init() {
        accept.addActionListener(this);
        cancel.addActionListener(this);
        accept.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        txthost.setText("localhost");
        String [] name = new String[10];
        name[0] = "Jorge";
        name[1] = "Sofia";
        name[2] = "Isabel";
        name[3] = "Daniel";
        name[4] = "David";
        name[5] = "Maria";
        name[6] = "Irene";
        name[7] = "Emma";
        name[8] = "Eva";
        name[9] = "Nacho";

        Random ran = new Random();
        txtName.setText(name[ran.nextInt(10)]);
    }

    public JPanel getPanel1() {
        setVisible(true);
        return panel1;
    }

    public static String getColor() {
        return color;
    }

    public static boolean isConnectionAcepted() {
        return connectionAcepted;
    }

    public static void setConnectionAcepted(boolean connectionAcepted) {
        Connection.connectionAcepted = connectionAcepted;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == accept){
            host();
            return;
        }
        if (actionEvent.getSource() == cancel){
            setVisible(false);
            return;
        }
    }

    private void host() {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(txthost.getText()) && !txthost.getText().equals("localhost")){
            JOptionPane.showMessageDialog(null, "URL no válida",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            connectionAcepted = false;
            return;
        }
        host = txthost.getText();
        nickname = txtName.getText();
        window.connectToServer();
        setVisible(false);
    }

    public static String getNickname() {
        return nickname;
    }

    public static String getHost() {
        return host;
    }
}
