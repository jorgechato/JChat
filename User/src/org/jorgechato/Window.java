package org.jorgechato;

import com.bolivia.label.CLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by jorge on 28/01/15.
 */
public class Window implements ActionListener,MouseListener{
    private JPanel chat;
    private JPanel panel1;
    private JPanel contactsRow;
    private JButton send;
    private JTextField textField1;
    private JPanel chatArea;
    private CLabel isConected;
    private JButton disconnect;
    private JScrollPane scrollarea;
    private Contact contact;
    private boolean connected;
    private Socket socket;
    private static int PORT = 6633;
    private PrintWriter out;
    private BufferedReader in;
    private String nickName,color;
    private static JFrame frame;
    private ArrayList<Contact> arrayUser;
    private int sliderValue = 0;
    public static ArrayList<Contact> outcast;

    public static void main(String[] args) {
        frame = new JFrame("JChat");
        frame.setContentPane(new Window().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public Window() {
        send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        isConected.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        disconnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        send.addActionListener(this);
        isConected.addMouseListener(this);
        disconnect.addActionListener(this);
        isConected.setBackground(Color.decode("#" + Contact.GREEN));

        chatArea.setLayout(new BoxLayout(chatArea,BoxLayout.Y_AXIS));
        contactsRow.setLayout(new BoxLayout(contactsRow,BoxLayout.Y_AXIS));

        arrayUser = new ArrayList<Contact>();
        outcast = new ArrayList<Contact>();

        isConnected();

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (Connection.isConnectionAcepted()) {
                    out.println("/typing");
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (Connection.isConnectionAcepted() && !textField1.getText().equals("")) {
                        out.println("/users " + textField1.getText());
                        out.print("/allniks");
                    }
                    textField1.setText("");
                }
            }
        });
        scrollarea.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                JScrollBar bar = scrollarea.getVerticalScrollBar();
                bar.setValue(e.getAdjustable().getMaximum());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == disconnect){
            disconnect();
        }
        if (actionEvent.getSource() == send){
            if (Connection.isConnectionAcepted() && !textField1.getText().equals("")) {
                out.println("/users " + textField1.getText());
                out.print("/allniks");
            }
            textField1.setText("");
        }
    }

    private void disconnect() {
        try {
            out.println("/quit");
            Connection.setConnectionAcepted(false);
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean isConnected() {
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                led();
            }

            private void led() {
                if (Connection.isConnectionAcepted()) {
                    disconnect.setSelected(false);
                    if (!isConected.getBackground().equals(Color.lightGray)) {
                        isConected.setBackground(Color.lightGray);
                    } else {
                        isConected.setBackground(Color.decode("#" + Contact.GREEN));
                        connected = true;
                    }
                }else{
                    isConected.setBackground(Color.decode("#" + Contact.RED));
                    disconnect.setSelected(true);
                }
            }
        });timer.start();

/*
        Timer usersOnLine = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(Connection.isConnectionAcepted() || (Connection.isConnectionAcepted() && textField1.getText().equals("")) )
                    out.println("/allniks");
            }
        });usersOnLine.start();*/

        return connected;
    }

    public void connectToServer(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String host = Connection.getHost();
                nickName = Connection.getNickname();
                color = Connection.getColor();
                char [] pass = Connection.getPass();

                serverConnection(host,nickName,color,pass);
                String name = "";
                String []typing;

                while (Connection.isConnectionAcepted()) {
                    try {
                        String conversation = in.readLine();
                        System.out.println(conversation);

                        if (conversation.startsWith("/nick")){
                            if(!name.equals(conversation) && name.length()!=conversation.length()) {
                                name = conversation;
                                getNicks(name);
                            }
                        }
                        else if (conversation.startsWith("/typing")){
                            typing = conversation.split(" ");
                            for (Contact contact : arrayUser) {
                                if (contact.getNick().equalsIgnoreCase(typing[1])) {
                                    contact.getTyping().setVisible(true);
                                }
                                reloadInterface();
                            }
                        }
                        else if (conversation.startsWith("/users")) {
                            sendToAll(conversation);
                        }
                        else if(conversation.startsWith("/sendto")){
                            String sendTo [];
                            sendTo = conversation.split("/");
                            boolean spam = false;
                            for (Contact contact1 : outcast){
                                if (contact1.getNick().equalsIgnoreCase(sendTo[1])){
                                    spam = true;
                                }
                            }
                            if (spam)
                                directMessage(sendTo[1],sendTo[2],sendTo[3]);
                        }
                    }catch (Exception e) {
                        Connection.setConnectionAcepted(false);
                    }
                }
            }

            private void directMessage(String from,String to,String message) {
                //todo
            }

            private void reloadInterface() {
                contactsRow.removeAll();
                for (Contact contact : arrayUser) {
                    contactsRow.add(contact.getPanel1());
                }
                contactsRow.updateUI();
            }

            private void sendToAll(String message) {
                String mess [] = message.split(" ");
                String conver = "";
                for (int i = 4; i < mess.length; i++) {
                    conver += mess[i]+" ";
                }
                Message message1 = new Message(mess[2],mess[1],conver);
                if (mess [1].equals(nickName)) {
                    message1.getMessage().setHorizontalAlignment(SwingConstants.RIGHT);
                    message1.getNick().setHorizontalAlignment(SwingConstants.RIGHT);
                }
                chatArea.add(message1.getPanel1());
                chatArea.updateUI();
            }

            private void getNicks(String name) {
                contactsRow.removeAll();
                String niksVector []= name.split(",");
                arrayUser = new ArrayList<Contact>();
                for (int i = 1; i < niksVector.length; i += 2) {
                    if (!nickName.equals(niksVector[i])) {
                        Contact contact = new Contact(niksVector[i],niksVector[i+1]);
                        contact.getTyping().setVisible(false);
                        arrayUser.add(contact);
                    }
                }
                reloadInterface();
            }
        });thread.start();
    }

    public void serverConnection(String host,String nick, String color, char [] pass) {
        try {
            socket = new Socket(host, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Connection.setConnectionAcepted(true);

            out.println(nick);
            out.println(pass);
            out.println(color);
            frame.setTitle("Hey "+nickName.toUpperCase()+" !");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() == isConected){
            showConnectios();
            return;
        }
    }

    private void showConnectios() {
        new Connection(this).getPanel1();
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

    public static void addToBlackList(Contact contact){
        outcast.add(contact);
    }
    public static void removeFromBlackList(Contact contact){
        outcast.remove(contact);
    }
}
