package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
public class Main extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	
	private Boolean  matrix[][];
	private ActorRef actors[];
	private int taille;
	public Main() {
		taille=5;
		matrix=new Boolean[taille][taille];
		actors=new ActorRef[taille];
		for(int i=0;i<taille;i++){
			for(int j=0;j<taille;j++){
				matrix[i][j]=false;
			}
		}
		for(int i=0;i<taille;i++){
			actors[i]=getContext().actorOf(Actor.createActor(), "actor".concat(Integer.toString(i)));
		}
		matrix[0][1]=true;
		matrix[0][2]=true;
		matrix[1][3]=true;
		matrix[2][3]=true;
		matrix[3][4]=true;
		matrix[4][1]=true;




	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Main.class, () -> {
			return new Main();
		});
	}
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			Message m = (Message) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			if (m.data.equals("SENDCONNECTIONS")) {
				for(int i=0;i<taille;i++){
					ArrayList<ActorRef> l=new ArrayList<ActorRef>();
					for(int j=0;j<taille;j++){
						if (matrix[i][j]){
							l.add(actors[j]);
						}
					}
					Message m0=new Message("CONNECTIONS",l);
					actors[i].tell(m0,getSelf());
				}
			}
			if (m.data.equals("START")) {
				Message m0=new Message("INIT");
				actors[0].tell(m0,getSelf());
			}
		}
	}

}
