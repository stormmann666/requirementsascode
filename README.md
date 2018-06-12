# requirements as code
This project aims to simplify developing an event-driven application.

The project provides a concise way to create handlers for many types of events at once.
A single runner receives events, and dispatches them to the handlers. That can be used for replay in event sourced applications.

For more advanced scenarios that depend on the application's state, 
you create a [use case model with flows](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld).
It's an easy alternative to state machines,
understandable by developers and business people alike.

Use a ```TestModelRunner``` instance to record events and steps and verify they match your expectations. See the [Hello World tests](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/test/java/helloworld/HelloWorldTest.java) for examples.

For the long term maintenance of your application,
you [generate documentation](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract) 
from the models inside the code without the need to add comments to it.

You can also handle [cross-cutting concerns](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns) in a simple way, for example for measuring performance, or for validation in event sourced applications.

# getting started
At least Java 8 is required, download and install it if necessary.

Requirements as code is available on Maven Central.

If you are using Maven, include the following in your POM, to use the core:

``` xml
  <dependency>
    <groupId>org.requirementsascode</groupId>
    <artifactId>requirementsascodecore</artifactId>
    <version>0.9.0</version>
  </dependency>
```

If you are using Gradle, include the following in your build.gradle, to use the core:

```
compile 'org.requirementsascode:requirementsascodecore:0.9.0'
```
# how to use requirements as code
Here's what you need to do as a developer:

## Step 1: Build a model defining the event classes to handle, and the methods that react to events:
``` java
Model model = Model.builder()
	.on(<event class>).system(<lambda, or reference To method that handles event>)
	.on(..).system(...)
	...
.build()
```

The order of the statements has no significance.
For handling exceptions instead of events, use the specific exception's class or `Throwable.class`.
Use `when` before `on` to define an additional condition that must be fulfilled.
You can also use `when` witout `on`, meaning: execute at the beginning of the run, or after a step has been run,
as soon as the condition is fulfilled.

## Step 2: Create a runner and run the model:
``` java
ModelRunner runner = new ModelRunner().run(model);
```

## Step 3: Send events to the runner, and enjoy watching it react:
``` java
runner.reactTo(<Event POJO Object>);
```
If an event's class is not declared in the model, the runner consumes it silently.
If an unchecked exception is thrown in one of the handler methods and it is not handled by any 
other handler method, the runner will rethrow it.

# hello world
Here's a complete Hello World example:

``` java
package hello;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class HelloUser {
	public static void main(String[] args) {
		Model model = Model.builder()
			.on(NameEntered.class).system(HelloUser::displayEnteredName)
		.build();

		new ModelRunner().run(model).reactTo(new NameEntered("Joe"));
	}

	public static void displayEnteredName(NameEntered nameEntered) {
		System.out.println("Hello, " + nameEntered.getUserName());
	}

	static class NameEntered {
		private String userName;

		public NameEntered(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return userName;
		}
	}
}
```

# documentation
* [Examples for building/running state based use case models](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld)
* [How to generate documentation from models](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract)
* [Cross-cutting concerns example](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns)

# publications
* [Simplifying an event sourced application](https://dev.to/bertilmuth/simplifying-an-event-sourced-application-1klp)
* [Kissing the state machine goodbye](https://dev.to/bertilmuth/kissing-the-state-machine-goodbye-34n9)
* [The truth is in the code](https://medium.freecodecamp.org/the-truth-is-in-the-code-86a712362c99)

# subprojects
* [requirements as code core](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): create and run models. 
* [requirements as code extract](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract): generate documentation from the models (or any other textual artifact).
* [requirements as code examples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirements as code.

# related topics
* The work of Ivar Jacobson on Use Cases. As an example, have a look at [Use Case 2.0](https://www.ivarjacobson.com/publications/white-papers/use-case-ebook).
* The work of Alistair Cockburn on Use Cases, specifically the different goal levels. Look [here](http://alistair.cockburn.us/Use+case+fundamentals) to get started, or read the book "Writing Effective Use Cases".
