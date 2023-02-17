package withAkka.commands;

import akka.actor.typed.ActorRef;

public class WhereIAm extends Command{

  private static final String WHERE_I_AM = "Where i am";
  private double value;

  public WhereIAm(ActorRef<ICommand> sender, double value) {
    super(WHERE_I_AM, sender);
    this.value= value;
  }

  public double getValue() {
    return value;
  }
}
