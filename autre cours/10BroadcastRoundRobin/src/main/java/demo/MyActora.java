package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActora extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef broadcaster;
	public MyActora() {}

	public MyActora(ActorRef broadcaster) {
		this.broadcaster=broadcaster;
		log.info("I was linked to actor reference {} and {}",this.broadcaster);
		
		
	}
	// Static function creating actor
	public static Props createActor(ActorRef broadcaster) {
		return Props.create(MyActora.class, () -> {
			return new MyActora(broadcaster);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("START")){
				Message m1=new Message("Hello comment ça va ?");
				this.broadcaster.tell(m1,getSelf());
			}
		}
	}

}
