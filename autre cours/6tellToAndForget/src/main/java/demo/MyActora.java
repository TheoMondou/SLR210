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

	public MyActora(ActorRef actorRefb,ActorRef actorReft) {
		this.actorRefb = actorRefb;
		this.actorReft = actorReft;
		log.info("I was linked to actor reference {} et {}", this.actorReft,this.actorRefb);
		Messagetransmit m=new Messagetransmit(this.actorRefb, "Hello");
		this.actorReft.tell(m,getSelf());
	}
	// Static function creating actor
	public static Props createActor(ActorRef actorRefb,ActorRef actorReft) {
		return Props.create(MyActora.class, () -> {
			return new MyActora(actorRefb,actorReft);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
	}

}
