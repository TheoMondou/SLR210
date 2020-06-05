package com.example;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.*;
import java.util.stream.Stream;


public class Main {

    public static int N = 100;
    public static int f=3;
    public static double tle=0.5;
    public static double probability_failure=0.0001;


    public static void main(String[] args) throws InterruptedException {

        // Instantiate an actor system
        final ActorSystem system = ActorSystem.create("system");
        system.log().info("System started with N=" + N );

        ArrayList<ActorRef> references = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            // Instantiate processes
            final ActorRef a = system.actorOf(Process.createActor(i + 1, N,probability_failure), "" + i);
            references.add(a);
        }

        //give each process a view of all the other processes
        Members m = new Members(references);
        for (ActorRef actor : references) {
            actor.tell(m, ActorRef.noSender());
        }


        Collections.shuffle(references);
        for (int i=0;i<f;i++){
            references.get(i).tell("crash", ActorRef.noSender());
        }
        for (int i=0;i<N;i++){
          references.get(i).tell("launch", ActorRef.noSender());
        }

        Thread.sleep((long) (tle*1000));
        for (int i=0;i<N-1;i++){                                      //le (f+1) est le leader
          references.get(i).tell("hold", ActorRef.noSender());
        }

        // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
    }
    public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}
}
