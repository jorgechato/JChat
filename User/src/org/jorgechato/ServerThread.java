package org.jorgechato;

import javax.swing.*;

/**
 * Created by jorge on 23/02/15.
 */
public class ServerThread extends Thread {
    private Window window;
    private String nickName,color;
    private Contact contact;

    public ServerThread(Window window) {
        this.window = window;
    }

    @Override
    public void run() {
        String host = Connection.getHost();
        nickName = Connection.getNickname();
        color = Connection.getColor();
        char [] pass = Connection.getPass();

        window.serverConnection(host, nickName, color, pass);
        String name = "";
        String []typing;

        while (Connection.isConnectionAcepted()) {
            try {
                String conversation = window.in.readLine();

                if (conversation.startsWith("/nick")){
                    if(!name.equals(conversation) && name.length()!=conversation.length()) {
                        name = conversation;
                        getNicks(name);
                    }
                }
                else if (conversation.startsWith("/typing")){
                    typing = conversation.split(" ");
                    for (Contact contact : window.getArrayUser()) {
                        if (contact.getNick().equalsIgnoreCase(typing[1])) {
                            contact.getTyping().setVisible(true);
                        }
                        reloadInterface();
                    }
                }
                else if (conversation.startsWith("/users")) {
                    sendToAll(conversation);
                }
                else if(conversation.startsWith("/directMessage")){
                    String sendTo [];
                    sendTo = conversation.split(";");
                    boolean spam = false;
                    for (Contact contact1 : window.getOutcast()){
                        if (contact1.getNick().equalsIgnoreCase(sendTo[2])){
                            spam = true;
                        }
                    }
                    if (!spam && !nickName.equalsIgnoreCase(sendTo[1]))
                        directMessage(sendTo[1],sendTo[2],sendTo[3]);
                }
            }catch (Exception e) {
                Connection.setConnectionAcepted(false);
            }
        }
    }

    private void directMessage(String from,String color,String message) {
        boolean sended = false;

        for (int i = 0 ; i<window.getTabbedPane1().getTabCount() ; i++){
            if (window.getTabbedPane1().getTitleAt(i).equalsIgnoreCase(from)){
                System.out.println("fda");
                window.addChat(new Message(color, from, message));
                sended = true;
            }
        }
        if (!sended){
            window.addChat(from);
            window.addChat(new Message(color, from, message));
        }
    }

    private void reloadInterface() {
        window.getContactsRow().removeAll();
        for (Contact contact : window.getArrayUser()) {
            window.getContactsRow().add(contact.getPanel1());
        }
        window.getContactsRow().updateUI();
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
        window.getChatArea().add(message1.getPanel1());
        window.getChatArea().updateUI();
    }

    private void getNicks(String name) {
        window.getContactsRow().removeAll();
        String niksVector []= name.split(",");
        window.getArrayUser().clear();
        for (int i = 1; i < niksVector.length; i += 2) {
            if (!nickName.equals(niksVector[i])) {
                contact = new Contact(niksVector[i],niksVector[i+1],window);
                contact.getTyping().setVisible(false);
                window.getArrayUser().add(contact);
            }
        }
        reloadInterface();
    }
}

