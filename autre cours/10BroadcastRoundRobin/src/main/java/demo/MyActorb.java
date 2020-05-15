package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActorb extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef broadcaster;
	public MyActorb(ActorRef broadcaster) {
		this.broadcaster=broadcaster;
	}


	// Static function creating actor
	public static Props createActor(ActorRef broadcaster) {
		return Props.create(MyActorb.class, () -> {
			return new MyActorb(broadcaster);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("START")) {
				Message m1=new Message("JOIN");
				this.broadcaster.tell(m1,getSelf());
			}
		}
	}

}
