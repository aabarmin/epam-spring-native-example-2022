package dev.abarmin.graalvm;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class MyCustomHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
//        hints.proxies().registerJdkProxy(MyCustomService.class);
//        hints.resources().registerPattern("resource-*.txt");
    }
}
