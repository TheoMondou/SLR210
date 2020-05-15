package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActora extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef actorRefc;
	private ActorRef actorRefb;
	public MyActora() {}

	public MyActora(ActorRef actorRefb,ActorRef actorRefc) {
		this.actorRefb = actorRefb;
		this.actorRefc = actorRefc;
		log.info("I was linked to actor reference {} and {}",this.actorRefb,this.actorRefc);
		
		
	}
	// Static function creating actor
	public static Props createActor(ActorRef actorRefb,ActorRef actorRefc) {
		return Props.create(MyActora.class, () -> {
			return new MyActora(actorRefb,actorRefc);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("START")){
				Message m1=new Message("Hello comment ça va ?",this.actorRefc);
				this.actorRefb.tell(m1,getSelf());
			}
		}
	}

}
