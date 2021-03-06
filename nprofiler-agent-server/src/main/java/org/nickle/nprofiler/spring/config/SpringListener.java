package org.nickle.nprofiler.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.nickle.nprofiler.registry.IRegistryClient;
import org.nickle.nprofiler.registry.RegistThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringListener {
    @Autowired
    private IRegistryClient registryClient;
    private RegistThread registThread;


    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (registThread == null) {
            registThread = new RegistThread(registryClient);
            registThread.start();
            return;

        }
        log.error("重复初始化");
    }

    @EventListener(ContextStoppedEvent.class)
    public void onApplicationEvent(ContextStoppedEvent event) {
        if (registThread != null) {
            registThread.interrupt();
        }
    }
}
