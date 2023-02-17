package withAkka.commands;

import akka.actor.typed.ActorRef;

public class StartTheRace extends Command{

  private static final String START = "Start";

  public StartTheRace(ActorRef<ICommand> sender) {
    super(START, sender);
  }
}
