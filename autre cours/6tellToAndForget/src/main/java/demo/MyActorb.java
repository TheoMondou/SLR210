package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActorb extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public MyActorb() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(MyActorb.class, () -> {
			return new MyActorb();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
		}
	}

}
