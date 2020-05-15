package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Message;
import java.util.ArrayList;
/**
 * @author Théo Mondou
 * @description
 */
public class PublishSubscribe {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate actors
		final ActorRef topic1 = system.actorOf(Topic.createActor(), "topic1");
		final ActorRef topic2 = system.actorOf(Topic.createActor(), "topic2");
		final ActorRef publisher1 = system.actorOf(Publisher.createActor(topic1), "publisher1");
		final ActorRef publisher2 = system.actorOf(Publisher.createActor(topic2), "publisher2");



		final ActorRef a = system.actorOf(A.createActor(), "a");
		final ActorRef b = system.actorOf(A.createActor(), "b");
		final ActorRef c = system.actorOf(A.createActor(), "c");
		ArrayList<ActorRef> list1=new ArrayList<ActorRef>();
		ArrayList<ActorRef> list2=new ArrayList<ActorRef>();
		ArrayList<ActorRef> list3=new ArrayList<ActorRef>();
		list1.add(topic1);
		list2.add(topic1);
		list2.add(topic2);
		list3.add(topic2);
		Message ma=new Message("SUBSCRIBE",list1);
		Message mab=new Message("SUBSCRIBE",list2);
		Message mb=new Message("SUBSCRIBE",list3);
		a.tell(ma,ActorRef.noSender());
		b.tell(mab,ActorRef.noSender());
		c.tell(mb,ActorRef.noSender());
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Message m0=new Message("START0");
		publisher1.tell(m0, ActorRef.noSender());

		Message m1=new Message("START1");
		publisher2.tell(m1, ActorRef.noSender());

	    try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Message m1a=new Message("UNSUBSCRIBE",list1);
		a.tell(m1a,ActorRef.noSender());

		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		Message m2=new Message("START2");
		publisher1.tell(m2, ActorRef.noSender());
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
