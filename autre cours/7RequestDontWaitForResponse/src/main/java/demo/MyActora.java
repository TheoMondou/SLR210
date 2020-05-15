package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActora extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef actorReft;
	private ActorRef actorRefb;
	public MyActora() {}

	public MyActora(ActorRef actorRefb) {
		this.actorRefb = actorRefb;
		log.info("I was linked to actor reference {}",this.actorRefb);
		Message m1=new Message("Hello comment ça va ?");
		this.actorRefb.tell(m1,getSelf());
		Message m2=new Message("Tu as fait quoi aujourd'hui ?");
		this.actorRefb.tell(m2,getSelf());
	}
	// Static function creating actor
	public static Props createActor(ActorRef actorRefb) {
		return Props.create(MyActora.class, () -> {
			return new MyActora(actorRefb);
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
