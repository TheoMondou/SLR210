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
    public final ActorRef actorref;
    public final int entier;
    public Message(String s) {
        this.data = s;
        this.actorref=null;
        this.entier=0;
    }
    public Message(String s,ActorRef a) {
        this.data = s;
        this.actorref=a;
        this.entier=0;
    }

    public Message(String s,int a) {
        this.data = s;
        this.actorref=null;
        this.entier=a;
    }

  }