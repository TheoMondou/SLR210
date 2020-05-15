package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
/**
 * @author Théo Mondou
 * @description
 */
public class Multicast {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef a = system.actorOf(MyActord.createActor(), "a");
		final ActorRef b = system.actorOf(MyActord.createActor(), "b");
		final ActorRef c = system.actorOf(MyActord.createActor(), "c");
		final ActorRef multicaster = system.actorOf(Multicaster.createActor(), "muticaster");
		final ActorRef sender = system.actorOf(Sender.createActor(multicaster,a,b,c), "sender");


		Message m0=new Message("START");
		sender.tell(m0, ActorRef.noSender());
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message m1=new Message("START1");
		sender.tell(m1, ActorRef.noSender());
		
	    
	
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
		Thread.sleep(6000);
	}

}
