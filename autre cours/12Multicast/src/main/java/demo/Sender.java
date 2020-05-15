package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
public class Sender extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef multicaster;
	private ActorRef a;
	private ActorRef b;
	private ActorRef c;
	public Sender() {}

	public Sender(ActorRef multicaster,ActorRef a,ActorRef b,ActorRef c) {
		this.multicaster=multicaster;
		this.a=a;
		this.b=b;
		this.c=c;
		
	}
	// Static function creating actor
	public static Props createActor(ActorRef multicaster,ActorRef a,ActorRef b,ActorRef c) {
		return Props.create(Sender.class, () -> {
			return new Sender(multicaster,a,b,c);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("START")){
				ArrayList<ActorRef> l1=new ArrayList<ActorRef>();
				ArrayList<ActorRef> l2=new ArrayList<ActorRef>();
				l1.add(a);
				l1.add(b);
				l1.add(c);
				l2.add(b);
				l2.add(c);
				Message m1=new Message("group1",l1);
				this.multicaster.tell(m1,getSelf());
				Message m3=new Message("group2",l2);
				this.multicaster.tell(m3,getSelf());
			}
			if (m.data.equals("START1")){
				Message m2=new Message("Hello","group1");
				this.multicaster.tell(m2,getSelf());
				Message m4=new Message("world","group2");
				this.multicaster.tell(m4,getSelf());
			}
		}
	}

}
