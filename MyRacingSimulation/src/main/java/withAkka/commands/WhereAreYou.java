package withAkka.commands;

import akka.actor.typed.ActorRef;

public class WhereAreYou extends Command{

  private static final String WHERE_ARE_YOU = "Where are you";

  public WhereAreYou(ActorRef<ICommand> sender) {
    super(WHERE_ARE_YOU, sender);
  }
}
