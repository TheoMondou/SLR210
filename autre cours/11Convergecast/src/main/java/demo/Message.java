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
    public final String type;
    public Message(String s) {
        this.data = s;
        this.actorref=null;
        this.type=null;
    }
    public Message(String s,ActorRef a) {
        this.data = s;
        this.actorref=org.apache.commons.lang3.SerializationUtils.clone(a);
        this.type=null;
    }
    public Message(String s,String t) {
        this.data = s;
        this.type=t;
        this.actorref=null;
    }

  }