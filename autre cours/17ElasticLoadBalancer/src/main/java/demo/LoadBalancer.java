package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
import java.util.HashMap;
public class LoadBalancer extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef actorRefc;
	HashMap<ActorRef,ArrayList<String>> dicotaches;
	ArrayList<ActorRef> listactors;
	int i;
	int taille;
	int idsession;

	public LoadBalancer() {
		listactors = new ArrayList<ActorRef>();
		dicotaches= new HashMap<ActorRef,ArrayList<String>>();
		i=0;
		taille=1;
		idsession=0;
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
			if (m.entier!=0){
				this.taille=m.entier;
			}
			else {
				if (m.data.startsWith("TRAVAIL")){
					if (listactors.size()<taille){
						idsession++;
						ActorRef a= getContext().actorOf(MyActorb.createActor(), "session".concat(Integer.toString(idsession)));
						listactors.add(a);
						ArrayList<String> l=new ArrayList<String>();
						l.add(m.data);
						dicotaches.put(a,l);
						a.tell(m,getSelf());
					}
					else{
						int min=1000;
						ActorRef a=listactors.get(0);
						for (ActorRef i : listactors) {
							if (dicotaches.get(i).size()<min){
								min=dicotaches.get(i).size();
								a=i;
							}
						}
						dicotaches.get(a).add(m.data);
						a.tell(m,getSelf());
					}
				}
				else {
					if (m.data.startsWith("FINISHED")){
						String s=m.data.substring(8);
						ArrayList<String> l=dicotaches.get(getSender());
						l.remove(s);
						if (l.size()==0){
							dicotaches.remove(getSender());
							listactors.remove(getSender());
							getSender().tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
						}
					}
					
				}
			}
		}
	}

}
