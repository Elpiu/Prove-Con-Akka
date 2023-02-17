package commands;

import akka.actor.typed.ActorRef;
import java.io.Serializable;

public interface ICommand extends Serializable {

  String getMessage();

  ActorRef<ICommand> getSender();
}
