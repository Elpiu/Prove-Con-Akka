package commands;

import akka.actor.typed.ActorRef;

public class StartCommand extends Command{

  public static final String START = "start";

  public StartCommand(ActorRef<ICommand> sender) {
    super(START, sender);
  }

}
