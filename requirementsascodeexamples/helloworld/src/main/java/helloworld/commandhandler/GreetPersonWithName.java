package helloworld.commandhandler;

import helloworld.domain.Greeting;
import helloworld.domain.Person;
import helloworld.infrastructure.OutputAdapter;

public class GreetPersonWithName implements Runnable{
  private Person person;
  private OutputAdapter outputAdapter;

  public GreetPersonWithName(Person person) {
    this.person = person;
    this.outputAdapter = new OutputAdapter();
  }

  @Override
  public void run() {
    greetWithName(person.getName());    
  }
  
  private void greetWithName(String name) {
    String greeting = Greeting.forUserWithName(name);
    outputAdapter.showMessage(greeting);
  }
}
