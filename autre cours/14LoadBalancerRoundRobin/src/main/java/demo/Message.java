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
    public Message(String s) {
        this.data = s;
        this.actorref=null;
    }
    public Message(String s,ActorRef a) {
        this.data = s;
        this.actorref=org.apache.commons.lang3.SerializationUtils.clone(a);
    }

  }