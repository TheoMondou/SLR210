package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActora extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef merger;
	public MyActora() {}

	public MyActora(ActorRef merger) {
		this.merger=merger;
		log.info("I was linked to actor reference {} and {}",this.merger);
		
		
	}
	// Static function creating actor
	public static Props createActor(ActorRef merger) {
		return Props.create(MyActora.class, () -> {
			return new MyActora(merger);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("START")){
				Message m0=new Message("JOIN");
				this.merger.tell(m0,getSelf());
			}
			if (m.data.equals("START1")){
				Message m1=new Message("Hello comment ça va ?","hi1");
				this.merger.tell(m1,getSelf());
			}
			if (m.data.equals("START3")){
				Message m2=new Message("Tu m'entends ?","hi2");
				this.merger.tell(m2,getSelf());
			}
		}
	}

}
