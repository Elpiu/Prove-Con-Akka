package withAkka.commands;

import akka.actor.typed.ActorRef;

public abstract class Command implements ICommand {

  protected String message;
  protected ActorRef<ICommand> sender;

  public Command(String message, ActorRef<ICommand> sender) {
    this.message = message;
    this.sender = sender;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public ActorRef<ICommand> getSender() {
    return sender;
  }
}
