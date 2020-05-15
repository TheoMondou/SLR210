package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
/**
 * @author Remi SHARROCK
 * @description
 */
public class Convergecast {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef d = system.actorOf(MyActord.createActor(), "d");
		final ActorRef merger = system.actorOf(Actormerger.createActor(d), "merger");
		final ActorRef a = system.actorOf(MyActora.createActor(merger), "a");
	    final ActorRef b = system.actorOf(MyActora.createActor(merger), "b");
		final ActorRef c = system.actorOf(MyActorb.createActor(merger), "c");

		Message m0=new Message("START");
		a.tell(m0, ActorRef.noSender());
		b.tell(m0, ActorRef.noSender());
		c.tell(m0, ActorRef.noSender());

		try {
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message m1=new Message("START1");
		a.tell(m1, ActorRef.noSender());
		b.tell(m1, ActorRef.noSender());
		c.tell(m1, ActorRef.noSender());

		try {
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Message m2=new Message("START2");
		c.tell(m2, ActorRef.noSender());

		try {
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}

	    Message m3=new Message("START3");
		a.tell(m3, ActorRef.noSender());
		b.tell(m3, ActorRef.noSender());

	    
	
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
