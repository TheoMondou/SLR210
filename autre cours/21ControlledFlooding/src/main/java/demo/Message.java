package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import akka.actor.ActorRef;
public class Message implements Serializable {
    private static final long serialVersionUID = 201912161042L;
    public final String data;
    public final ArrayList<ActorRef> actorref;
    public final int id;
    public Message(String s) {
        this.data = s;
        this.actorref=null;
        id=0;
    }
    public Message(String s,int a) {
        this.data = s;
        this.actorref=null;
        id=a;
    }
    public Message(String s,ArrayList<ActorRef> l) {
        this.data = s;
        this.actorref=l;
        id=0;
    }

  }