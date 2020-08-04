package com.yardi.ring;

import akka.actor.ActorRef;

public class SetNextNode {
    private ActorRef nextNode;

    public SetNextNode(ActorRef nextNode) {
        this.nextNode = nextNode;
    }

    public ActorRef getNextNode() {
        return nextNode;
    }
}
