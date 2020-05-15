package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SessionManager extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	public SessionManager() {
		number=1;
	}
	private int number;


	// Static function creating actor
	public static Props createActor() {
		return Props.create(SessionManager.class, () -> {
			return new SessionManager();
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("CREATESESSION")) {
				getContext().actorOf(Session.createActor(), "actor".concat(Integer.toString(number)));
				number=number+1;

			}

		}
	}

}
