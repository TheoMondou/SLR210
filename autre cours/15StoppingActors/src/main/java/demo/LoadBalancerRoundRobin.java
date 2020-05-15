package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
/**
 * @author Remi SHARROCK
 * @description
 */
public class LoadBalancerRoundRobin {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef loadbalancer = system.actorOf(LoadBalancer.createActor(), "loadbalancer");
		final ActorRef a = system.actorOf(MyActora.createActor(loadbalancer), "a");
		final ActorRef c = system.actorOf(MyActorb.createActor(loadbalancer), "c");
	    final ActorRef b = system.actorOf(MyActorb.createActor(loadbalancer), "b");
		Message m4=new Message("STARTJOIN");
		Message m5=new Message("STARTUNJOIN");
		b.tell(m4, ActorRef.noSender());
		c.tell(m4, ActorRef.noSender());
		try {
			Thread.sleep(400);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Message m0=new Message("Hello0");
		Message m1=new Message("Hello1");
		Message m2=new Message("Hello2");
		Message m3=new Message("Hello3");


		a.tell(m0, ActorRef.noSender());
		try {
			Thread.sleep(400);
		} catch (Exception e) {
			e.printStackTrace();
		}


		loadbalancer.tell(akka.actor.Kill.getInstance(), ActorRef.noSender());





		a.tell(m1, ActorRef.noSender());
		try {
			Thread.sleep(400);
		} catch (Exception e) {
			e.printStackTrace();
		}
		a.tell(m2, ActorRef.noSender());
		try {
			Thread.sleep(400);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.tell(m5, ActorRef.noSender());
		try {
			Thread.sleep(400);
		} catch (Exception e) {
			e.printStackTrace();
		}
		a.tell(m3, ActorRef.noSender());
		


	    
	
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
