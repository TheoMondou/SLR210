package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
public class LoadBalancer extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef actorRefc;
	ArrayList<ActorRef> list;
	int i;

	public LoadBalancer() {
		list = new ArrayList<ActorRef>();
		i=0;
	}
	// Static function creating actor
	public static Props createActor() {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer();
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
				if (m.data.equals("UNJOIN")){
					if (list.contains(getSender())){
						if (list.indexOf(getSender())<i){
							i--;
						}
						if (list.indexOf(getSender())==i && list.size()==i-1){
							i=0;
						}
						list.remove(getSender());
					}
				}
				if (!list.isEmpty()){
					ActorRef a =list.get(i);
					a.tell(m,getSender());
					i++;
					if (i>=list.size()){
						i=0;
					}
				}
				
			}
		}
	}

}
