# Spring Dynamic Message Processor
A Maven/Spring-Boot/Spring-Integration service for executing dynamically determined processing stage paths. 

This image describes the control flow through the core router/transformer at the heart of this application.
It does not describe the configuration of SQS or the server in which the application is hosted. 

![Dynamic Routing Spring Integration Core](dynamic_route.jpg)

## Configurations
The project comes with a pure java configuration for both core and test. 
Both configurations work, the core configuration is a prerequisite for the test configuration. 

# System Overview

## Settings 
Some meta-parameters needed at compile time are set in the file *Constants.java*, these includes header values, channel names, timeout values, and that sort of thing. 
*src/main/resources/application.yml* and *src/test/resources/application.yml* contain additional parameters necessary to initialize SQS connection(s).
*src/main/resources/log4j.xml* and *src/test/resources/log4j.xml* are configuration files for logging in both applications. 
All debug messages are printed through logging so all the prints can be controlled from these configuration files. 

## Full System Flow
Spring-boot takes care of launching the route/transform code as a server, and of launching scheduled (either periodically or event-driven) tasks to look at SQS queues that are specified in the yaml 
configurations described above. The route/transform service is intended to be accessed through a `RouteAndProcessService` service bean. 
Use-case examples for the full system flow (starting from pushing a message onto an inbound SQS queue) can be found in the file *com/calamp/connect/messageprocessor/BaseTestSuite.java*.
Examples using the core dynamic path execution service can be found in *com/calamp/connect/messageprocessor/domain/services/ScheduledTasks.java*.
Here is an example of fetching the core `RouteAndProcessServiceBean`:

```
    RouteAndProcessService service = context.getBean(serviceName, RouteAndProcessService.class);
```

It can also be autowired assuming that the class this code is in is marked `@Component` or `@Service`:

```
    @Autowired 
    RouteAndProcessService service;
```

Here is an example (from *com/calamp/connect/messageprocessor/domain/services/ScheduledTasks.java*) of using the core service to asynchronously launch a dynamic execution path program:

```
    Future<ProcessingWrapper<E>> ret = service.processMessage(payload);
```

This will execute the asynchronous process of `enact`ing the stages (with dynamic path changes). 
To retrieve the reply (and synchronize):

```
    ProcessingWrapper<E> = ret.get()
```

In the previous example `payload` is of type *com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper.java*
A user will wrap the inbound deserialized object (retrieved from SQS and then deserialized) in a `ProcessingWrapper` object using the method below from `com.calamp.connect.messageprocessor.Util`:

```
    public static <E> ProcessingWrapper<E> wrapData(E dataPayload, List<String> initialPathPlan) {
        UUID siId = UUID.randomUUID();
        ProcessingWrapper<E> payload = new ProcessingWrapper<E>(siId, dataPayload, initialPathPlan);
        return payload;
    }
```

Object deserialization is done thus (from `com.calamp.connect.messageprocessor.domain.services.ScheduledTasks`):

```
    E reconstructed = this.sds.stringToObject(message);
```

Where `sds` is an autowired `com.calamp.connect.messageprocessor.domain.services.SerializeDeserializeService`:

```
    @Autowired(required = true)
    SerializeDeserializeService sds;
```

The `SerializeDeserializeService` can serialize and deserialize arbitrary `Serialiazable` java classes so if `NetworkAdapter` and `MessageProcessor` use 
the same `SerializeDeserializeService` and data classes are `Serializable` then the services can communicate successfully.  

```
    List<String> initialPathPlan;
```

Is a sequence of `stage`s (as identified by unique `String` identifiers) this represents the initial execution `stage` plan that the inbound deserialized object will follow.
The plan is retrieved using the `com.calamp.connect.messageprocessor.domain.services.PathInitializationService` thus:

```
    List<String> initialPathPlan = pathServe.initializePath(payload);
```

Again assuming:

```
    @Autowired(required = true)
    PathInitializationServiceInterface pathServe;
```

To be able to initialize a path given an inbound object the relevent path mapping must have been registered with the `PathInitializationService` as a bootstrapped execution phase,
for example from `BaseTestSuite`:

```
    @Before
    public void setUp() throws Exception {
        this.pathServe.register(StaticPathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_B", "DummyStage_D", "DummyStage_E")); 
        this.pathServe.register(ExpandingPathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_B", "DummyStage_D", "DummyStage_E", "DummyStage_F")); 
        this.pathServe.register(ClearFuturePathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_G", "DummyStage_B", "DummyStage_D", "DummyStage_E"));
        this.pathServe.register(ExceptionPathClass.class, Arrays.asList("DummyStage_B", "DummyStage_A", "DummyStage_H", "DummyStage_D"));
    }
```

The `PathInitializationService` registers classes to label lists that specify the initial execution path that a class of a given type should follow. 

The reply wrapper returned by `ProcessingWrapper<E> = ret.get()` may either be a valid reply containing relevant data, or it wraps an exception that was thrown during the processing of the request. 

Replies are processed by a `com.calamp.connect.messageprocessor.domain.services.ReplyProcessService`. 
The active `ReplyProcessService` is tasked with managing `Future<ProcessingWrapper<E>>` returned by `RouteAndProcessService` depending on application needs the 
`Future`s may be lazily evaluated, eagerly evaluated or not evaluated. Two examples of `ReplyProcessingService`s are `com.calamp.connect.messageprocessor.domain.services.ReplyProcessService`, and `com.calamp.connect.messageprocessor.domain.services.TestingReplyProcessService`. `ReplyProcessService` is a no-op service; it simply logs its use and does nothing with incomming `Future<ProcessingWrapper<E>>`.
`TestingReplyProcessService` eagerly retrieves the `ProcessingWrapper<E>` an pushes it onto a `BlockingQueue` to be retrieved and validated during testing. 

## Daemon Config
*ScheduledTasks.java* is the entry point for the control flow of the program; 
it implements either periodically launched execution tasks (e.g poll SQS) or listeners which launch tasks based on some environmental 
conditions (e.g a message arrives at SQS). Examples in `BaseTestSuite` implement an example network and injects example messages into the network 
(by pushing messages into an SQS queue on which a listener from `ScheduledTasks` is listening). Execution `stage`s must extend 
`com.calamp.connect.messageprocessor.domain.stages.Stage` and then be initialized as `@Bean` in a .java configuration file. 
The reason to initialize them separately rather than declare stages as `@Service` is that there may be multiple beans of the same class 
but executing potentially differently configured versions of the same service. 

## SI Packet Wrapper Config
All inbound packets to be routed through the designated Spring Integration (SI) network should be wrapped in a `ProcessingWrapper` object (inside the `dataPayload` field). 
The wrapped object can be reconstructed into the desired object through a serialize/deserialize implementation as described above. 
The `ProcessingWrapper` object also contains routing and exception information for the SI-network.

## Stage Config
Each execution `stage` required in this spring system should inherit from the abstract class `Stage` and implement an `enact` method. 
Each such method will perform the required transform on the ProcessingWrapper message that it receives (as needed on the internal packet data contained therein).
Each such method then returns another `ProcessingWrapper` containing the outbound data from the implementing stage. 
`DummyStage` is an example implementation of such a stage.   

## Exception Handling Config
`Exception`s resulting from execution of the `stage`s are handled by the exception handler in `ErrorHandlerService`, it can be extended as desired. 
`Exception`s will still be returned as a reply to the calling service (wrapped in a `ProcessingWrapper`) this is to prevent blocking on a service 
request terminated due to an exception.

## End Path Handling Config
All messages upon completion of their path arrive at `com.calamp.connect.messageprocessor.service_activators.ServiceActivators`,
the class therein may be extended beyond the current logging function to perform other useful work needed upon path completion.

# Core Route/Transform Service Details
In this section I will give a high level overview of some of the details of what's going on in the core routing service.

## Route/Transform Details 
Execution progresses as a series of Route-Transform calls. Each deserialized incoming message is wrapped in a `ProcessingWrapper` 
and sent along its retreived initialization path (a sequence of `Stage`s) each `stage` is `enact`ed upon the `ProcessingWrapper`. 
A `ProcessingWrapper` contains a `dataPayload`, a `transitPath`, and a `futurePath`.
The `dataPayload` is the deserialized inbound message from `NetworkAdapter`. The transitPath is a `final` field that indicates the 
path taken by the `ProcessingWrapper` thus far. The `futurePath` represents the 
execution plan following the current `stage`. Each `stage` may reconfigure the `futurePath` as desired to modify which stages will get 
executed and in what order. Enacting a `stage` entails two steps:

1. The `dataPayload` of the `ProcessingWrapper` is modified as desired by the `enact`ing stage (the core service of the `stage` is executed). 

2. The `futurePath` of the `ProcessingWrapper` is modified to schedule any dynamically determined execution path `stage`s that during 
the `enact` operation of the executing stage were created or discovered as needed. Or of-course `enact` may remove previously scheduled `stage`s. 
Note that this step involves a `processingWrapper.advance(this.getStageIdentifer());` operation; if this isn't done then an infinite loop of size 1 results. 
See `com.calamp.connect.messageprocessor.domain.stages` for some example stages, and notice that all stages perform the `processingWrapper.advance(this.getStageIdentifer());` 
operation first.

The router makes a binary decision. Either processing continues or processing stops. If processing continues (`futurePath.length() > 0`) then a transform operation is performed. 
A transform operation will choose the correct stage to `enact` next (based on the value of the label in `futurePath.get(0)` ). 
The stages will register themselves automatically with the `Transformers` during construction (this code is in *Stage.java*). Processing concludes after the last stage 
`enact`s making `futurePath.length() == 0` finally as the last step of this execution flow a method of the user's choice many be activated from: 
`com.calamp.connect.messageprocessor.service_activators.ServiceActivators` by applying the annotation `@ServiceActivator(inputChannel = Constants.targetChannelName)` 
to the concluding method. If at any `stage` an `exception` is encountered then the `exception` is sent to 
`com.calamp.connect.messageprocessor.domain.services.handleErrorMessage(Message<Exception> errorMessage)` it can be acted on from there as desired. 
If one wanted to change behavior based on the type of the exception then this would be the method to do it in. 
`Stage` execution for an inbound message will continue until either the `futurePath.length() == 0` of the wrapping `ProcessingWrapper` or until an `Exception` is encountered.

# Advice

The easiest way to understand the system is to look at the tests in `BaseTestSuite` and trace the execution path from there.
