package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Transmitter extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Transmitter() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Transmitter.class, () -> {
			return new Transmitter();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Messagetransmit){
			Messagetransmit m = (Messagetransmit) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"] for actor ["+m.actorRef+"]");
			Message m2=new Message(m.data);
			m.actorRef.tell(m2,getSender());
		}
	}

}
