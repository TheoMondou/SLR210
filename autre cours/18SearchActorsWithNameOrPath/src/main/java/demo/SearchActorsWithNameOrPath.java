package demo;
import akka.actor.ActorSelection;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
import akka.actor.Props;
import akka.util.Timeout;
import java.util.concurrent.TimeUnit;
import scala.concurrent.Await;
/**
 * @author Théo Mondou
 * @description
 * Wow j'ai galéré beaucoup à faire le peu qui suit.
 * J'ai rien compris à la doc, du coup j'utilise des classes compliquées comme Future
 * Je suis incapable de trouver les acteurs actorX uniquement par leur nom, j'ai besoin de préciserqu'ils sont sous a

 */
public class SearchActorsWithNameOrPath {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef a = system.actorOf(SessionManager.createActor(), "a");

		Timeout timeout = new Timeout(1000, TimeUnit.MILLISECONDS);

		
		Message m0=new Message("CREATESESSION");
		a.tell(m0,ActorRef.noSender());
		a.tell(m0,ActorRef.noSender());
		
		//system.actorSelection("../a");
		scala.concurrent.Future<akka.actor.ActorRef> futur1=system.actorSelection("**/a").resolveOne(timeout);
		scala.concurrent.Future<akka.actor.ActorRef> futur2=system.actorSelection("**/a/actor1").resolveOne(timeout);
		scala.concurrent.Future<akka.actor.ActorRef> futur3=system.actorSelection("**/a/actor2").resolveOne(timeout);
		//scala.concurrent.Future<akka.actor.ActorRef> futur4=system.actorSelection("/user/**").resolveOne(timeout);

		try {
			ActorRef reply1 = (ActorRef)Await.result(futur1, timeout.duration());
			ActorRef reply2 = (ActorRef)Await.result(futur2, timeout.duration());
			ActorRef reply3 = (ActorRef)Await.result(futur3, timeout.duration());
			System.out.println("Path : "+reply1.path());
			System.out.println("Path : "+reply2.path());
			System.out.println("Path : "+reply3.path());
		} catch (Exception e) {
			e.printStackTrace();
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
