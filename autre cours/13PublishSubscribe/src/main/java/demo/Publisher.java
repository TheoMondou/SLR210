package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
public class Publisher extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef topic;
	public Publisher() {}

	public Publisher(ActorRef topic) {
		this.topic=topic;	
	}
	// Static function creating actor
	public static Props createActor(ActorRef topic) {
		return Props.create(Publisher.class, () -> {
			return new Publisher(topic);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("START0")){
				Message m0=new Message("hello");
				topic.tell(m0,getSelf());
			}
			if (m.data.equals("START1")){
				Message m0=new Message("world");
				topic.tell(m0,getSelf());
			}
			if (m.data.equals("START2")){
				Message m0=new Message("hello2");
				topic.tell(m0,getSelf());
			}
		}
	}

}
