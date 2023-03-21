package withAkka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import util.MyPair;
import withAkka.commands.Command;
import withAkka.commands.ICommand;
import withAkka.commands.RacerFinished;
import withAkka.commands.StartRunning;
import withAkka.commands.StartTheRace;
import withAkka.commands.WhereAreYou;
import withAkka.commands.WhereIAm;

public class ControllerBehavior extends AbstractBehavior<ICommand> {

  private Object TIMER_KEY;

  private Map<ActorRef<ICommand>, Integer> currentPositions;
  private Queue<MyPair<ActorRef<ICommand>, Long>> finishingTimes;

  private Long start;
  private int raceLenght = 40;

  private int NUM_RACERS = 5;

  private ControllerBehavior(ActorContext<ICommand> context) {
    super(context);
    currentPositions = new HashMap<>();
    Comparator<MyPair<ActorRef<ICommand>, Long>> finishingTimeComparator = new Comparator<MyPair<ActorRef<ICommand>, Long>>() {
      @Override
      public int compare(MyPair<ActorRef<ICommand>, Long> pair1, MyPair<ActorRef<ICommand>, Long> pair2) {
        return pair1.getSecond().compareTo(pair2.getSecond());
      }
    };

    finishingTimes = new PriorityQueue<>(finishingTimeComparator);
  }

  public static Behavior<ICommand> create() {
    return Behaviors.setup(ControllerBehavior::new);
  }

  @Override
  public Receive<ICommand> createReceive() {
    return newReceiveBuilder().onMessage(StartTheRace.class, command -> {
      start = System.currentTimeMillis();
      initAllRacer();
      tellToStartToAllRacer();
      //return this;

      return Behaviors.withTimers(timer -> {
        timer.startTimerAtFixedRate(TIMER_KEY, new GetPositionsCommand(getContext().getSelf()), Duration.ofSeconds(1));
        return this;
      });
    }).onMessage(WhereIAm.class, command -> {
      currentPositions.put(command.getSender(), (int) command.getValue());
      return this;
    }).onMessage(GetPositionsCommand.class, command -> {
      currentPositions.forEach((x, integer) -> {
        x.tell(new WhereAreYou(getContext().getSelf()));
        displayRace();
      });
      return this;
    }).onMessage(RacerFinished.class, command -> {
      finishingTimes.add(new MyPair<>(command.getSender(), System.currentTimeMillis() - start));
      currentPositions.remove(command.getSender());
      //getContext().stop(command.getSender());
      if (currentPositions.isEmpty()) {
        return Behaviors.withTimers(timers -> {
          timers.cancelAll();
          System.out.println("\n".repeat(10));
          finishingTimes.forEach(x -> {
            System.out.println("||Racer :"+ x.getFrist().path().name() + "||  ||Time : " + x.getSecond()+"||");
          });

          return Behaviors.stopped();
        });
      }
      return this;
    }).build();
  }

  private void initAllRacer() {
    for (int i = 0; i < NUM_RACERS; i++) {
      ActorRef<ICommand> racer = getContext().spawn(RacerBehavior.create(), "Racer" + i + 1);
      currentPositions.put(racer, 0);
    }
  }

  private void tellToStartToAllRacer() {
    currentPositions.forEach((x, y) -> x.tell(new StartRunning(getContext().getSelf(), raceLenght)));
  }

  //We want the controller periodically check current position of all racers.
  private class GetPositionsCommand extends Command {

    private static final String GET_CURRENT_POSITION = "Get positions";

    public GetPositionsCommand(ActorRef<ICommand> sender) {
      super(GET_CURRENT_POSITION, sender);
    }
  }

  private void displayRace() {
    int displayLength = 160;
    for (int i = 0; i < 50; ++i) {
      System.out.println();
    }
    System.out.println("Race has been running for " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
    System.out.println("    " + new String(new char[displayLength]).replace('\0', '='));
    int i = 0;
    for (ActorRef<ICommand> racer : currentPositions.keySet()) {
      System.out.println(i + " : " + new String(new char[currentPositions.get(racer) * displayLength / 100]).replace('\0', '*'));
      i++;
    }
  }

}

