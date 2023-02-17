package commands;

import akka.actor.typed.ActorRef;

public class InitCommand extends Command{

  private static final String INIT = "init";

  public InitCommand(ActorRef<ICommand> sender) {
    super(INIT, sender);
  }
}
