package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.*;
public class Actormerger extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef actorRefd;
	ArrayList<ActorRef> list;
	Hashtable<String,Hashtable<ActorRef,Message>> dic;

	public Actormerger(ActorRef actorRefd) {
		this.actorRefd=actorRefd;
		dic= new Hashtable<String,Hashtable<ActorRef,Message>>();
		list=new ArrayList<ActorRef>();
	}
	// Static function creating actor
	public static Props createActor(ActorRef actorRefd) {
		return Props.create(Actormerger.class, () -> {
			return new Actormerger(actorRefd);
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
					list.remove(getSender());
					for (Map.Entry mapentry : dic.entrySet()) {
						if (dic.containsKey(getSender())){
							dic.remove(getSender());
						}
					 }
				}
				else {
					String type=m.type;
					if (dic.containsKey(type)){

					}
					else {
						dic.put(type,new Hashtable<ActorRef,Message>());
					}
					Hashtable<ActorRef,Message> h=dic.get(type);
					if (h.containsKey(getSender())){
						h.replace(getSender(),m);
					}
					else {
						h.put(getSender(),m);
					}
					boolean verif=true;
					String messagegroupe="";
					for (ActorRef a : list){
						if (h.containsKey(a)){
							messagegroupe+="\n"+a.path().name()+": "+h.get(a).data;
						}
						else {
							verif=false;
						}
					}
					if (verif){
						Message m0=new Message(messagegroupe);
						this.actorRefd.tell(m0,getSelf());
						h.remove(type);
					}
				}
			}

		}
	}

}
