package akkaactors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import commands.ICommand;
import commands.InitCommand;
import commands.ResultCommand;
import commands.StartCommand;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

//AbstractBehavior<String> messaggio che si aspetta di ricevere
public class ManagerBehavior extends AbstractBehavior<ICommand> {

  private static final String NAME_OF_WORKER = "Worker_";
  private static final int NUMBER_OF_WORKER = 20;

  private int intWorkerNumber;

  HashMap<String, ActorRef<ICommand>> mapOfWorker;

  SortedSet<String> bigIntegers;

  private ManagerBehavior(ActorContext<ICommand> context) {
    super(context);
    intWorkerNumber = 0;
    mapOfWorker = new HashMap<>();
    bigIntegers = new TreeSet<>();
  }

  public static Behavior<ICommand> create() {
    return Behaviors.setup(ManagerBehavior::new);
  }

  @Override
  public Receive<ICommand> createReceive() {
    return newReceiveBuilder().onMessage(InitCommand.class, command -> {
      initActors();
      return this;
    }).onMessage(StartCommand.class, command -> {
      startActors();
      return this;
    }).onMessage(ResultCommand.class, command -> {
      BigInteger bigProbPrime =  command.getResultNumber();
      bigIntegers.add(command.getSender().path() + " Result: " + String.valueOf(bigProbPrime));
      System.out.println("Size List: " + bigIntegers.size() + ".");
      if (bigIntegers.size() == 20) {
        System.out.println("-----------------------");
        bigIntegers.stream().forEach(System.out::println);
        System.out.println("-----------------------");
      }
      return this;
    }).build();
  }

  private void initActors() {
    System.out.println("I'm inizializing everything");
    for (int i = 0; i < NUMBER_OF_WORKER; i++) {
      String name = NAME_OF_WORKER + Integer.toString(getAndIncrement());
      //getContext().spawn(akkaactors.WorkerBehavior.create(), name)
      mapOfWorker.put(name, ActorSystem.create(WorkerBehavior.create(), name));
    }
  }

  private void startActors() {
    mapOfWorker.forEach((key, value) -> {
      value.tell(new StartCommand(getContext().getSelf()));
    });
  }

  private int getAndIncrement() {
    intWorkerNumber++;
    return intWorkerNumber - 1;
  }
}
