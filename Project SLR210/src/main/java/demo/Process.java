package com.example;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Process extends UntypedAbstractActor {
    public static final class ReadMsg {
        private int ballot;
        public int get_ballot() {return ballot;}
        public ReadMsg(int ballot) {ballot = ballot;}
    }
    public static final class GatherMsg {
        private int ballot;
        private int imposeballot;
        private int estimate;
        public int get_ballot() {return ballot;}
        public int get_imposeballot() {return imposeballot;}
        public int get_estimate() {return estimate;}
        public GatherMsg(int ballot, int imposeballot, int estimate) {
            ballot = ballot;
            imposeballot = imposeballot;
            estimate = estimate;
        }
    }
    public static final class ImposeMsg {
        private int ballot;
        private int value;
        public int get_ballot() {return ballot;}
        public int get_value() {return value;}
        public ImposeMsg(int ballot, int value) {
            ballot = ballot;
            value = value > 0;//value can only be 0 or 1
        }
    }
    public static final class DecideMsg {
        private int value;
        public int get_value() {return value;}
        public DecideMsg(int value) {
            value = value > 0;//value can only be 0 or 1
        }
    }
    public static final class AbortMsg {
        private int ballot;
        public int get_ballot() {return ballot;}
        public AbortMsg(int ballot) {ballot = ballot;}
    }
    public static final class AckMsg {
        private int ballot;
        public int get_ballot() {return ballot;}
        public AckMsg(int ballot) {ballot = ballot;}
    }
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);// Logger attached to actor
    private final int N;//number of processes
    private final int id;//id of current process
    private Members processes;//other processes' references
    private Integer proposal;
    private int ballot;
    private int readballot;
    private int imposeballot;
    private Integer estimate;
    private int states[][] ;

    public Process(int ID, int nb) {
        N = nb;
        id = ID;
        ballot =id-N;
        proposal=null;
        readballot=0;
        imposeballot=id-N;
        estimate=null;
        states = new int[N][2];
    }
    
    public String toString() {
        return "Process{" + " id = " + id + " };
    }

    /**
     * Static function creating actor
     */
    public static Props createActor(int ID, int nb) {
        return Props.create(Process.class, () -> {
            return new Process(ID, nb);
        });
    }
    
    private void ofconsProposeReceived(Integer v) {
        proposal = v;
        for (ActorRef actor : processes.references) {
            actor.tell(new ReadMsg(ballot), this.getSelf());
            log.info("Read ballot " + ballot + " msg: p" + self().path().name() + " -> p" + actor.path().name());
        }
    }

    private void readReceived(int newBallot, ActorRef pj) {
            log.info("read received " + self().path().name() );
    }

    public void onReceive(Object message) throws Throwable {
        if (message instanceof Members) {//save the system's info
            Members m = (Members) message;
            processes = m;
            log.info("p" + self().path().name() + " received processes info");
        } else if (message instanceof OfconsProposerMsg) {
            OfconsProposerMsg m = (OfconsProposerMsg) message;
            this.ofconsProposeReceived(m.v);
        } else if (message instanceof ReadMsg) {
            ReadMsg m = (ReadMsg) message;
            this.readReceived(m.ballot, getSender());
        } 
    }
}
