package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SessionManager extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	public SessionManager() {}
	public ActorRef session1;


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
				this.session1= getContext().actorOf(Session.createActor(), "session1");
				Message m1=new Message("session1",this.session1);
				getSender().tell(m1,getSelf());
			}
			if (m.data.equals("ENDSESSION")) {
				this.session1.tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
			}
		}
	}

}
