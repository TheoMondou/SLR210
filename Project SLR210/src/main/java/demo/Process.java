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
        public ImposeMsg() {}
    }
    public static final class DecideMsg {
        public DecideMsg() {}
    }
    public static final class AbortMsg {
        public AbortMsg() {}
    }
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);// Logger attached to actor
    private final int N;//number of processes
    private final int id;//id of current process
    private Members processes;//other processes' references
    private int ballot;
    private String proposal;
    private int readballot;
    private int imposeballot;
    private String estimate;
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
        return "Process{" + "id=" + id ;
    }

    /**
     * Static function creating actor
     */
    public static Props createActor(int ID, int nb) {
        return Props.create(Process.class, () -> {
            return new Process(ID, nb);
        });
    }
    
    public void onReceive(Object message) throws Throwable {
    	
          if (message instanceof Members) {//save the system's info
              Members m = (Members) message;
              processes = m;
              log.info("p" + self().path().name() + " received processes info");
          }
          else if (true){

          }
      
    }
}
