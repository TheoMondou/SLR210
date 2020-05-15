package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
public class Actorbroadcast extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef actorRefc;
	ArrayList<ActorRef> list;

	public Actorbroadcast() {
		list = new ArrayList<ActorRef>();
	}
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Actorbroadcast.class, () -> {
			return new Actorbroadcast();
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("JOIN")){
				list.add(getSender());
			}
			else {
				for(ActorRef a : list){
					a.tell(m,getSender());
				}
			}
		}
	}

}
