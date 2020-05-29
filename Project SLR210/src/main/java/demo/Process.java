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
    public static final class AbortMsg {
        private int ballot;
        public int get_ballot() {return ballot;}
        public AbortMsg(int ballot) {ballot = ballot;}
    }
    public static final class Abort extends Exception {
        private int ballot;
        public int get_ballot() {return ballot;}
        public Abort(int ballot) {
          super();
          ballot = ballot;
       }
    }
  
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
    public static final class Decide extends Exception {
        private Integer ballot;
        public Integer get_ballot() {return ballot;}
        public Decide(int ballot) {
          super();
          ballot = ballot;
       }
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
    private int ack_count;
    private int readballot;
    private int imposeballot;
    private Integer estimate;
    private int states[][] ;
    private int gather_count;

    public final int get_id() {return id;}
    public Process(int ID, int nb) {
        N = nb;
        id = ID;
        ballot =id-N;
        ack_count = 0;
        proposal=null;
        readballot=0;
        imposeballot=id-N;
        estimate=null;
        states = new Integer[N][2];
        for (int i = 0; i < N; i++) {
          states[i][0] = null;
          states[i][1] = 0;
        }
        ack_count = 0;
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
        if (readballot > newBallot || imposeballot > newBallot) {
            pj.tell(new AbortMsg(newBallot), this.getSelf());
            log.info("Abort ballot " + newBallot + " msg: p" + self().path().name() + " -> p" + pj.path().name());
        } else {
            readballot = newBallot;
            pj.tell(new GatherMsg(newBallot, imposeballot, estimate), this.getSelf());
            log.info("Gather ballot " + newBallot + " msg: p" + self().path().name() + " -> p" + pj.path().name());
        }
    }

    private void gatherReceived(int est, int estballot, ActorRef pj) {
        gather_count++;
        log.info("gather received " + self().path().name() );
        states[pj.get_id()] = [est, estballot];
        if (gather_count > N/2) {
            int max = 0;
            for ( int[2] state : states) 
                if (state[1] > max) {
                    proposal = state[0];
                    max = state[1];
                }
            for (int i = 0; i < N; i++) {
                states[i][0] = null;
                states[i][1] = 0;
            }
            log.info("impose sent " + self().path().name() );
            // send_to_all(new ImposeMsg(ballot, proposal), this.getSelf());
        }
    }

    private void imposeReceived(int newBallot, int value, ActorRef pj) {
        log.info("impose received " + self().path().name() );
        if (readballot > newBallot || imposeballot > newBallot) {
            pj.tell(new AbortMsg(newBallot), this.getSelf());
            log.info("Abort ballot " + newBallot + " msg: p" + self().path().name() + " -> p" + pj.path().name());
        } else {
            estimate = value;
            imposeballot = newBallot;
            pj.tell(new AckMsg(newBallot), this.getSelf());
            log.info("Impose ballot " + newBallot + " msg: p" + self().path().name() + " -> p" + pj.path().name());
        }
    }

    private void decideReceived(Integer value) throws Decide {
        log.info("Decide: " + value + " -> " + self().path().name() );
        // send_to_all(new DecideMsg(value), this.getSelf());
        throw new Decide(value);
    }

    private void ackReceived(int ballot) {
        log.info("ack received " + self().path().name() );
        ack_count++;
        if (ack_count > N/2) {
            // send_to_all(new DecideMsg(proposal), this.getSelf());
            ack_count = 0; // to avoid sending the message too many times
        }
    }

    private void abortReceived(int ballot) throws Abort {
        log.info("abort received " + self().path().name() );
        throw new Abort(ballot);
    }

    public void onReceive(Object message) throws Throwable {
        if (message instanceof Members) {//save the system's info
            Members m = (Members) message;
            processes = m;
            log.info("p" + self().path().name() + " received processes info");
        } else if (message instanceof OfconsProposerMsg) {
            OfconsProposerMsg m = (OfconsProposerMsg) message;
            this.ofconsProposeReceived(m.message);
        } else if (message instanceof ReadMsg) {
            ReadMsg m = (ReadMsg) message;
            this.readReceived(m.get_ballot(), getSender());
        } else if (message instanceof GatherMsg) {
            GatherMsg m = (GatherMsg) message;
            this.gatherReceived(m.get_estimate(), m.get_imposeballot(), getSender());
        } else if (message instanceof ImposeMsg) {
            ImposeMsg m = (ImposeMsg) message;
            this.imposeReceived(m.get_ballot(), m.get_value(), getSender());
        } else if (message instanceof DecideMsg) {
            DecideMsg m = (DecideMsg) message;
            this.decideReceived(m.get_value());
        } else if (message instanceof AbortMsg) {
            AbortMsg m = (AbortMsg) message;
            this.abortReceived(m.get_ballot());
        } else if (message instanceof AckMsg) {
            AckMsg m = (AckMsg) message;
            this.ackReceived(m.get_ballot());
        }
    }
}
