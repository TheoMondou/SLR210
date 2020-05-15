package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Session extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	public Session() {}


	// Static function creating actor
	public static Props createActor() {
		return Props.create(Session.class, () -> {
			return new Session();
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("Hello !")) {
				Message m1=new Message("Hello comment ça va ?");
				getSender().tell(m1,getSelf());
			}
			
		}
	}

}
