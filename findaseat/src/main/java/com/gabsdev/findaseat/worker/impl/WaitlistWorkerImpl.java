package com.gabsdev.findaseat.worker.impl;

import com.gabsdev.findaseat.model.entity.Waitlist;
import com.gabsdev.findaseat.service.WaitlistService;
import com.gabsdev.findaseat.worker.WaitlistWorker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class WaitlistWorkerImpl implements WaitlistWorker {
    private final WaitlistService service;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public WaitlistWorkerImpl(WaitlistService service) {
        this.service = service;
    }


    @Scheduled(fixedRate = 180000)
    @Override
    public void verifyStatusWaitlist() {
        service.verifyStatusWaitlist();
    }

    @Scheduled(fixedRate = 60000)
    @Override
    public void verifyQueue() {
       List<Waitlist> waitlistList =service.getWaitlist();
       waitlistList.forEach(service::verifySeatsAvaliable);
    }
}
