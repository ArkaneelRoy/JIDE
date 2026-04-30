package com.willow.androidide.ultra.test;

import java.util.function.Consumer;

public class Main {
    
    private void testLambda () {
        consume (e -> {
            e.@@cursor@@
        });
    }
    
    private void consume (Consumer<Throwable> consumer) {
        consumer.accept (new RuntimeException ("Hello world!"));
    }
}
