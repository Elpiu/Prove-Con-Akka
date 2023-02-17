package commands;

import akka.actor.typed.ActorRef;
import java.math.BigInteger;

public class ResultCommand extends Command{

  private static String RESULT = "result";
  private BigInteger resultNumber ;

  public ResultCommand(ActorRef<ICommand> sender, BigInteger resultNumber) {
    super(RESULT, sender);
    this.resultNumber = resultNumber;
  }

  public BigInteger getResultNumber() {
    return resultNumber;
  }
}
