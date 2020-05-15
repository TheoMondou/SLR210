package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
/**
 * @author Théo Mondou
 * J'ai fusionné le main et l'acteur a pour une question de simplicité
 * @description
 */
public class ElasticLoadBalancer {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef loadbalancer = system.actorOf(LoadBalancer.createActor(), "loadbalancer");
		final ActorRef a = system.actorOf(MyActora.createActor(loadbalancer), "a");

		Message m0=new Message("START0");
		Message m1=new Message("START1");
		Message m2=new Message("START2");
		Message m3=new Message("START3");


		a.tell(m0, ActorRef.noSender());
		try {
			Thread.sleep(400);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
