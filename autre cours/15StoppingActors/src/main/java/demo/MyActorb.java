package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActorb extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef loadbalancer;
	public MyActorb(ActorRef loadbalancer) {
		this.loadbalancer=loadbalancer;
	}


	// Static function creating actor
	public static Props createActor(ActorRef loadbalancer) {
		return Props.create(MyActorb.class, () -> {
			return new MyActorb(loadbalancer);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("STARTJOIN")) {
				Message m1=new Message("JOIN");
				this.loadbalancer.tell(m1,getSelf());
			}
			if (m.data.equals("STARTUNJOIN")) {
				Message m1=new Message("UNJOIN");
				this.loadbalancer.tell(m1,getSelf());
			}
		}
	}

}
