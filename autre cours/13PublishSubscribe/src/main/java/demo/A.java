package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
public class A extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	public A() {}


	// Static function creating actor
	public static Props createActor() {
		return Props.create(A.class, () -> {
			return new A();
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("SUBSCRIBE")){
				if (m.list!=null){
					for (ActorRef a : m.list){
						Message m0=new Message("SUBSCRIBE");
						a.tell(m0,getSelf());
					}
				}
			}
			if (m.data.equals("UNSUBSCRIBE")){
				if (m.list!=null){
					for (ActorRef a : m.list){
						Message m0=new Message("UNSUBSCRIBE");
						a.tell(m0,getSelf());
					}
				}
			}
		}
	}

}
