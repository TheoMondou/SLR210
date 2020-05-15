package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
public class Actor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ArrayList<ActorRef> neighbours;
	private int nombreconnections;
	private ArrayList<Integer> idrecus;
	public Actor() {
		idrecus=new ArrayList<Integer>();
	}


	// Static function creating actor
	public static Props createActor() {
		return Props.create(Actor.class, () -> {
			return new Actor();
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.actorref!=null) {
				neighbours=m.actorref;
				nombreconnections=neighbours.size();
				Message m1=new Message("Hello !");
				for (ActorRef a : neighbours){
					a.tell(m1,getSelf());
				}
			}
			else{
				if (m.data.equals("INIT")){
					int n= (int) Math.random() * ( 10000 )+1;
					idrecus.add(new Integer(n));
					Message m1=new Message("Envoie ceci à tout le monde",n);
					for (ActorRef a : neighbours){
						a.tell(m1,getSelf());
					}
				}
				else{
					if (m.id!=0){
						int n=m.id;
						if (!idrecus.contains(new Integer(n))){
							idrecus.add(new Integer(n));
							Message m1=new Message(m.data,n);
							for (ActorRef a : neighbours){
								a.tell(m1,getSelf());
							}
						}
					}
					
				}
			}
			
		}
	}

}
