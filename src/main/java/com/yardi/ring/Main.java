package com.yardi.ring;

public class Main {

    public static final long RING_SIZE = 500000;

    public static void main(String[] args) {
        long ringSize = RING_SIZE;
        Ring ring = new Ring(ringSize);
        String message = "some message";

        // Where should timing go? inside or outside?
        long duration = time(() -> ring.blockingPass(message));
        System.out.println(
                String.format(
                        "It took %d milliseconds for message to pass around %s nodes in a ring.",
                        duration,
                        ringSize));
    }

    private static long time(Runnable runnable) {
        try {
            long start = System.currentTimeMillis();
            runnable.run();
            long end = System.currentTimeMillis();
            return end - start;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

