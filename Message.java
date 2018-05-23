package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    Object o=new Object();
    Object o1=new Object();
    Object o2=new Object();
    Object o3=new Object();
    MessageType type;

    public Message(MessageType type, Object o) {
        this.type = type;
        this.o=o;
    }
    public Message(MessageType type, Object o1, Object o2)
    {
        this.type=type;
        this.o1=o1;
        this.o2=o2;
    }
    public Message(MessageType type, Object o1, Object o2, Object o3)
    {
        this.type=type;
        this.o1=o1;
        this.o2=o2;
        this.o3=o3;
    }

    public MessageType getType() {
        return this.type;
    }

    public Object getObject(){
        return this.o;
    }

    public Object getObject1(){
        return this.o1;
    }

    public Object getObject2(){
        return this.o2;
    }

    public Object getObject3(){
        return this.o3;
    }

}