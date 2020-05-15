package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
/**
 * @author Théo Mondou
 * @description

 */
public class SessionChildActor {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef sessionmanager = system.actorOf(SessionManager.createActor(), "sessionmanager");
	    final ActorRef client1 = system.actorOf(Client.createActor(sessionmanager), "client1");
		
		Message m0=new Message("START0");
		client1.tell(m0,ActorRef.noSender());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	    Message m1=new Message("START1");
		client1.tell(m1,ActorRef.noSender());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	    Message m2=new Message("START2");
		client1.tell(m2,ActorRef.noSender());
	
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
