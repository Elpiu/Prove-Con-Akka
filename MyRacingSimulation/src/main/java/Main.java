import akka.actor.typed.ActorSystem;
import withAkka.ControllerBehavior;
import withAkka.commands.ICommand;
import withAkka.commands.StartTheRace;

public class Main {
    public static void main(String[] args) {
        ActorSystem<ICommand> raceController = ActorSystem.create(ControllerBehavior.create(), "RaceSimulation");
        raceController.tell(new StartTheRace(null));
    }
}
