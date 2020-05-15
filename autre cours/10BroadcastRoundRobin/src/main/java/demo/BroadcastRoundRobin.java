package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
/**
 * @author Remi SHARROCK
 * @description
 */
public class BroadcastRoundRobin {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef broadcaster = system.actorOf(Actorbroadcast.createActor(), "broadcaster");
		final ActorRef a = system.actorOf(MyActora.createActor(broadcaster), "a");
		final ActorRef c = system.actorOf(MyActorb.createActor(broadcaster), "c");
	    final ActorRef b = system.actorOf(MyActorb.createActor(broadcaster), "b");
		Message m0=new Message("START");
		b.tell(m0, ActorRef.noSender());
		c.tell(m0, ActorRef.noSender());
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    a.tell(m0, ActorRef.noSender());

	    
	
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
