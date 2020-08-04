package com.yardi.ring;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.time.Duration;

public class Ring {

    private final ActorRef startingNode;

    public Ring(long nNodes) {
        ActorSystem ring = ActorSystem.create("ring");

        ActorRef endingNode = null;
        ActorRef nextActor = null;

        for (long i = nNodes; i > 0; i--) {
            String actorName = "node-" + i;
            Props props = Props.create(NodeActor.class, nextActor);
            ActorRef actorRef = ring.actorOf(props, actorName);
            if (endingNode == null) {
                endingNode = actorRef;
            }
            nextActor = actorRef;
        }
        // actorRef is the starting node
        startingNode = nextActor;

        ActorRef sender = ActorRef.noSender();
        if (endingNode != null) {
            endingNode.tell(new SetNextNode(startingNode), sender);
        }
    }

    public void blockingPass(String message) {
        Timeout timeout = Timeout.create(Duration.ofSeconds(5));
        Future<Object> future = Patterns.ask(startingNode, message, timeout);
        try {
            String result = (String) Await.result(future, timeout.duration());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // result should be orginal message
    }
}
