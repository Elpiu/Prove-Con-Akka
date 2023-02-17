package withAkka.commands;

import akka.actor.typed.ActorRef;

public class StartRunning extends Command{

  private static final String START_RACE = "Start Race";
  private int raceLength;


  public StartRunning(ActorRef<ICommand> sender, int raceLenght) {
    super(START_RACE, sender);
    this.raceLength = raceLenght;
  }

  public int getRaceLength() {
    return raceLength;
  }
}
