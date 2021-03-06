package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActora extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef loadbalancer;
	public MyActora() {}

	public MyActora(ActorRef loadbalancer) {
		this.loadbalancer=loadbalancer;
		log.info("I was linked to actor reference {} and {}",this.loadbalancer);
		
		
	}
	// Static function creating actor
	public static Props createActor(ActorRef loadbalancer) {
		return Props.create(MyActora.class, () -> {
			return new MyActora(loadbalancer);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			this.loadbalancer.tell(m,getSelf());
		}
	}

}
