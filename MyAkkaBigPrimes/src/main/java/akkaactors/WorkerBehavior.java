package akkaactors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import commands.ICommand;
import commands.ResultCommand;
import commands.StartCommand;
import java.math.BigInteger;
import java.util.Random;

public class WorkerBehavior extends AbstractBehavior<ICommand> {

  private WorkerBehavior(ActorContext<ICommand> context) {
    super(context);
  }

  public static Behavior<ICommand> create() {
    return Behaviors.setup(WorkerBehavior::new);
  }

  @Override
  public Receive<ICommand> createReceive() {
    return newReceiveBuilder().onMessage(StartCommand.class, command -> {
      BigInteger bigProbPrime = findBigPrimeNumber();
      command.getSender().tell(new ResultCommand(getContext().getSelf(), bigProbPrime));
      return this;
    }).build();
  }

  private BigInteger findBigPrimeNumber() {
    BigInteger bigInteger = new BigInteger(2000, new Random());
    return bigInteger.nextProbablePrime();
  }

}
