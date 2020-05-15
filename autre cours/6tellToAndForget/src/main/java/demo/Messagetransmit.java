package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import akka.actor.ActorRef;

public class Messagetransmit implements Serializable {
    private static final long serialVersionUID = 201912161037L;
    public final ActorRef actorRef;
    public final String data;

    public Messagetransmit(ActorRef actorRef, String data) {
        this.actorRef = actorRef;
        this.data = data;
    }

  }