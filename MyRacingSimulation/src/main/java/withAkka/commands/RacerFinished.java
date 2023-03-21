package withAkka.commands;

import akka.actor.typed.ActorRef;

public class RacerFinished extends Command{

  private static final String FINISHED_COMMAND_RACER = "Start Race";


  public RacerFinished(ActorRef<ICommand> sender) {
    super(FINISHED_COMMAND_RACER, sender);
  }
}
