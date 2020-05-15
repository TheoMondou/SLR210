package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Client extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef sessionmanager;
	private ActorRef session1;
	public Client() {}

	public Client(ActorRef sessionmanager) {
		this.sessionmanager = sessionmanager;
		log.info("I was linked to actor reference {}",this.sessionmanager);
		
	}
	// Static function creating actor
	public static Props createActor(ActorRef sessionmanager) {
		return Props.create(Client.class, () -> {
			return new Client(sessionmanager);
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			
			if (m.data.equals("START0")){
				Message m1=new Message("CREATESESSION");
				this.sessionmanager.tell(m1,getSelf());
			}
			else{
				if (m.data.equals("START1")){
					Message m1=new Message("ENDSESSION");
					this.sessionmanager.tell(m1,getSelf());
				}
				else{
					if (m.data.equals("START2")){
						Message m1=new Message("Tu es toujours là ?");
						this.session1.tell(m1,getSelf());
					}
					else {
						if (m.actorref!=null) {
							this.session1=m.actorref;
							Message m1=new Message("Hello !");
							this.session1.tell(m1,getSelf());
						}
						else{
							Message m1=new Message("Bien et toi ?");
							this.session1.tell(m1,getSelf());
						}
					}
				}
				
			}
	
		}
	}

}
