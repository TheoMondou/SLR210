package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.*;
public class Topic extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	ArrayList<ActorRef> abonnes;

	public Topic() {
		abonnes= new ArrayList<ActorRef>();
	}
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Topic.class, () -> {
			return new Topic();
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data=="SUBSCRIBE"){
				if (this.abonnes.contains(getSender())){
				}
				else {
					this.abonnes.add(getSender());

				}
			}
			else{
				if (m.data=="UNSUBSCRIBE"){
					if (this.abonnes.contains(getSender())){
						this.abonnes.remove(getSender());
					}
				}
				else {
					for (ActorRef a:abonnes){
						a.tell(m,getSender());
					}
				}
			}
			

		}
	}

}
