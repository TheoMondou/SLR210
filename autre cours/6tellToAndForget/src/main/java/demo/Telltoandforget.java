package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Messagetransmit;
import demo.Message;
/**
 * @author Remi SHARROCK
 * @description
 */
public class Telltoandforget {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef b = system.actorOf(MyActorb.createActor(), "b");
		final ActorRef t = system.actorOf(Transmitter.createActor(), "t");
	    final ActorRef a = system.actorOf(MyActora.createActor(b,t), "a");
	    

	    
	
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
