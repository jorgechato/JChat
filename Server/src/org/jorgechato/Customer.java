package org.jorgechato;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jorge on 27/01/15.
 */
public class Customer extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Main server;
    private String nik,pass,color;
    private boolean outcast;
    public static Logger log4j = Logger.getLogger(Customer.class.getName());

    public Customer(Socket socket,Main server) throws IOException{
        this.socket = socket;
        this.server = server;

        writer = new PrintWriter(socket.getOutputStream(),true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        try {
            log4j.addAppender(new FileAppender(new PatternLayout(), "JChat.log", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNik() {
        return nik;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public void run() {
        writer.println("/server Tu eres " + socket.getInetAddress().getHostName());
        try{
            String nick = reader.readLine();
            setNik(nick);
            String pass = reader.readLine();
            setPass(pass);
            String color = reader.readLine();
            setColor(color);

            log4j.info("Nuevo usuario: "+nick);
//            writer.println("/server Hay " + server.numCustomer() + " usuarios conectados");

            server.sendAllNicksToAllUsers();
            String line = null;
            while ((line = reader.readLine()) != null){
                if (line.equals("/quit")){
                    socket.close();
                    server.removeCustomer(this);
                    server.sendAllNicksToAllUsers();
                    log4j.info("Usuario desconectado: "+nick);
                    break;
                }
                else if (line.equals("/typing")){
                    server.sendToAll("/typing " + nick);
                }
                else if (line.startsWith("/users")) {
                    server.sendToAll("/users " + nick + " " + color + " " + line);
                }
                else if (line.startsWith("/directMessage")) {
                    String message [] = line.split(";");
                    server.sendToUser( message[1] ,nick + ";" + color + ";" + message[2]);
                }
                else if (line.startsWith("/allniks")) {
                    server.sendAllNicksToAllUsers();
                }
            }
        }catch (Exception e){
//            e.printStackTrace();
            server.removeCustomer(this);
            server.sendAllNicksToAllUsers();
        }
    }
}
