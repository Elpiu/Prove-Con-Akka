import akka.actor.typed.ActorSystem;
import akkaactors.ManagerBehavior;
import commands.ICommand;
import commands.InitCommand;
import commands.StartCommand;

public class Main {

  public static void main(String[] args) {
    ActorSystem<ICommand> actorSystemOne =
      ActorSystem.create(ManagerBehavior.create(), "BigPrimesManager");
    //actorSystemOne.tell("Hello are you there?");
    actorSystemOne.tell(new InitCommand(null));
    actorSystemOne.tell(new StartCommand(null));
  }

}
