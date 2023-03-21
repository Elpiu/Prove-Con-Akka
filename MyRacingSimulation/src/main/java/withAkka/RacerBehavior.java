package withAkka;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import java.util.Random;
import withAkka.commands.ICommand;
import withAkka.commands.RacerFinished;
import withAkka.commands.StartRunning;
import withAkka.commands.WhereAreYou;
import withAkka.commands.WhereIAm;

public class RacerBehavior extends AbstractBehavior<ICommand> {

  private final double defaultAverageSpeed = 48.2;
  private int averageSpeedAdjustmentFactor;
  private Random random;

  private double currentSpeed = 0;
  private double currentPosition = 0;
  private int raceLength;

  private RacerBehavior(ActorContext<ICommand> context) {
    super(context);
  }

  public static Behavior<ICommand> create() {
    return Behaviors.setup(RacerBehavior::new);
  }

  @Override
  public Receive<ICommand> createReceive() {
    return newReceiveBuilder().onMessage(StartRunning.class, command -> {
      start(command.getRaceLength());
      return this;
    }).onMessage(WhereAreYou.class, command -> {
      double myPosituon = whereIAm();
      command.getSender().tell(new WhereIAm(getContext().getSelf(), myPosituon));
      if (myPosituon == raceLength) {
        command.getSender().tell(new RacerFinished(getContext().getSelf()));
        //Ignora i prossimi messaggi e vai idle
        return Behaviors.ignore();
      }
      return this;
    }).onSignal(PostStop.class, signal -> {
      System.out.println(signal);
      return Behaviors.same();
    }).build();
  }

  private void start(int lenght) {
    raceLength = lenght;
    random = new Random();
    averageSpeedAdjustmentFactor = random.nextInt(30) - 10;
  }

  private double whereIAm() {
    determineNextSpeed();
    currentPosition += getDistanceMovedPerSecond();
    if (currentPosition > raceLength) {
      currentPosition = raceLength;
    }
    return currentPosition;
  }

  private double getMaxSpeed() {
    return defaultAverageSpeed * (1 + ((double) averageSpeedAdjustmentFactor / 100));
  }

  private double getDistanceMovedPerSecond() {
    return currentSpeed * 1000 / 3600;
  }

  private void determineNextSpeed() {
    if (currentPosition < (raceLength / 4)) {
      currentSpeed = currentSpeed + (((getMaxSpeed() - currentSpeed) / 10) * random.nextDouble());
    } else {
      currentSpeed = currentSpeed * (0.5 + random.nextDouble());
    }

    if (currentSpeed > getMaxSpeed()) {
      currentSpeed = getMaxSpeed();
    }

    if (currentSpeed < 5) {
      currentSpeed = 5;
    }

    if (currentPosition > (raceLength / 2) && currentSpeed < getMaxSpeed() / 2) {
      currentSpeed = getMaxSpeed() / 2;
    }
  }

}
