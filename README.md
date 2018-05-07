# AoseDevKit2018

The AoseDevKit2018 framework has been implemented for the course of Agent Oriented Software Engineering at the University Of Trento (2018).
The framework consists of three repositories:
- *AoseDevKit2018-MultiAgentSystem: https://github.com/marcorobol/AoseDevKit2018-MultiAgentSystem*
- *AoseDevKit2018-Generic: https://github.com/marcorobol/AoseDevKit2018-Generic*
- *AoseDevKit2018-Blocksworld: https://github.com/marcorobol/AoseDevKit2018-Blocksworld*

Introductory slides on the framework are available in the doc folder in the AoseDevKit2018-MultiAgentSystem repository.

In the case of bugs to any of these, let us know or fix them and do a pull request.

## Installing and running

Prerequisites: Git + Eclipse + JDK 1.8

This repository depends on *AoseDevKit2018-Generic* and *AoseDevKit2018-MultiAgentSystem*
so be sure to import all of them into your Eclipse workspace.
To do so follow these steps:

1. Fork and clone all the 3 repositories:
    > $ git clone xxx
2. Import the projects in Eclipse:
    > File -> Import -> Existing Projects into Workspace
3. **[Only for Linux/Mac users]** Give execution permission to blackbox executable in 2018-AoseDevKit-Blocksworld/blackbox:
    > $ chmod +x blackbox
4. **[Only if you have multiple versions of Java]** In the case Eclipse detects errors in the projects
    - in the Eclipse preferences set default compiler to Java 1.8
    - or right click on each of them and select:
      > Properties -> Java Compiler -> Enable project specific setting -> Compiler 1.8
  or 
5. To run right click on file unitn.adk2018.blocksworld.BlocksworldLauncher and select:
    > Run as -> Java Application

# AoseDevKit2018-MultiAgentSystem

This project is the core and the development kit of the multi-agent system of the AoseDevKit2018 framework. It does not contains any implementations of goal or intention.

Hare a brief description of the main classes and packages:

- **unitn.adk2018.Agent** is the base class of any agent, and provides the following methods:
    - timer management:
        - long getAgentTime ()
        - void setPauseState ( boolean doPause )
        - boolean isPaused ()
        - boolean rescheduleTimer(Observer o, long when)
        - boolean removeFromScheduledTimers(Observer o)
    - agent configuration
        - getName ()
        - boolean addSupportedEvent ( Class<E> event, Class<Intention> handler )
    - goals and messages sumbission
        - boolean pushMessage ( Message msg )
        - boolean pushGoal ( Event goal, MaintenanceCondition _asLongAs )
    - scheduling of waiting intentions
        - boolean rescheduleIntention(ScheduledIntention si)
        - boolean removeFromScheduledIntentions(ScheduledIntention si)
    - execution
        - void startInSeparateThread()
- **unitn.adk2018.Environment** is the entry point, implemented using the singleton pattern, for everything in the simulation
    - PddlDomain
    - SystemTimer
    - Agents
    - EnvironmentAgent
    - Messages dispatcher
-  **unitn.adk2018.condition** is the package that contains observers of different type of events from timeout to goal state. To be used in the implementation of intention, to implement wait for or wait until logics.
- **unitn.adk2018.event.Goal .InformMessage .RequestMessage** are the three base classes to be used in the development of goals and messages.
- **unitn.adk2018.intention.Intention** is the base class to be used in the development of intentions.
- **unitn.adk2018.pddl** is the package to that contains domain-independent pddl utility methods.






