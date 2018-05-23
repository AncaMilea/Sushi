package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Comms {

    protected static final int PORT = 25410;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;
    protected boolean connected;


    public static int getPort() {
        return PORT;
    }

    public Comms() {

        connected = true;
    }

    public boolean sendMessage(Message m) {
        try {
            out.writeObject(m);
            out.reset();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public Message receiveMessage() {
        try {
            return (Message) in.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Invalid message type");
        } catch (IOException e) {
            System.err.println("Connection lost");
        }
        return null;
    }

}