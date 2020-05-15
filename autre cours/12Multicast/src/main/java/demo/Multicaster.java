package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.*;
public class Multicaster extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	Hashtable<String,ArrayList<ActorRef>> dic;

	public Multicaster() {
		dic= new Hashtable<String,ArrayList<ActorRef>>();
	}
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Multicaster.class, () -> {
			return new Multicaster();
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.list!=null){
				if (this.dic.contains(m.data)){
					this.dic.replace(m.data,m.list);
				}
				else {
					dic.put(m.data,m.list);

				}
			}
			else {
				if (m.type!=null){
					if (this.dic.containsKey(m.type)){
						
						
						ArrayList<ActorRef> list=this.dic.get(m.type);
						for (ActorRef a:list){
							Message m0=new Message(m.data);
							a.tell(m0,getSender());
						}
					}

				}
			}

		}
	}

}
