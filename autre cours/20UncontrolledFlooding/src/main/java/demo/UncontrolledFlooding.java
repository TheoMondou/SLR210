package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
/**
 * @author Théo Mondou
 * @description

 */
public class UncontrolledFlooding {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef main = system.actorOf(Main.createActor(), "main");
		Message m0=new Message("SENDCONNECTIONS");
		main.tell(m0,ActorRef.noSender());
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message m1=new Message("START");
		main.tell(m1,ActorRef.noSender());

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
