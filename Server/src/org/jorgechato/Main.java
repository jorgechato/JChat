package org.jorgechato;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    private int port;
    private ServerSocket socket;
    private ArrayList<Customer> arrayCustomer;

    public Main (int port){
        this.port = port;
        arrayCustomer = new ArrayList<Customer>();
    }

    public void connect() throws IOException{
        socket = new ServerSocket(port);
    }

    public boolean isConnected(){
        return !socket.isClosed();
    }

    //escucha a que se conecte alguien
    public Socket listen() throws IOException {
        return socket.accept();
    }

    public void addCustomer(Customer customer){
        arrayCustomer.add(customer);
    }

    public void removeCustomer(Customer customer){
        arrayCustomer.remove(customer);
    }

    public void sendToAll(String message){
        for (Customer customer : arrayCustomer){
            customer.getWriter().println(message);
        }
    }

    public String getAllNicks(){
        String nicks = "/nick,";
        for (Customer customer : arrayCustomer){
            nicks += customer.getNik()+","+ customer.getColor() +",";
        }
        return nicks;
    }

    public void sendAllNicksToAllUsers(){
        for (Customer customer : arrayCustomer){
            customer.getWriter().println(getAllNicks());
        }
    }

    public int numCustomer(){
        return arrayCustomer.size();
    }

    public void disconect() throws IOException {
        socket.close();
    }
}
