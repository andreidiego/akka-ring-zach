package com.yardi.ring;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class NodeActor extends AbstractActor {

    private ActorRef nextNode;

    public NodeActor(ActorRef nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public Receive createReceive() {
        return forwarding();
    }

    // State of Actor: FORWARDING
    // Handles ALL incoming messages
    private Receive forwarding() {
        return receiveBuilder()
                .match(
                        String.class,
                        (message) -> {
                            nextNode.tell(message, this.self()); // forward message to next node
                            this.getContext().become(replying(this.sender()));
                        })
                .match(
                        SetNextNode.class,
                        (cmd) -> {
                            this.nextNode = cmd.getNextNode();
                        }
                )
                .build();
    }

    // State of Actor: REPLYING
    private Receive replying(ActorRef originalSender) {
        return receiveBuilder()
                .match(
                        String.class,
                        (message) -> {
                            originalSender.tell(message, this.self());
                            this.getContext().become(forwarding());
                        })
                .build();
    }


}
