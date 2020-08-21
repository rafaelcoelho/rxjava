package com.sandbox.rxjava;

import static com.sandbox.rxjava.util.Utils.sleepOneSecond;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Scheduler.Worker;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class Schedule
{
    public static void main(String[] args)
    {
        final Scheduler schedule = Schedulers.trampoline();
        final Worker worker = schedule.createWorker();

        log.debug("Main start");

        worker.schedule(() -> {
            log.debug(" Outer start");
            sleepOneSecond();

            worker.schedule(() -> {
                log.debug("  Inner start");
                sleepOneSecond();
                log.debug("  Inner end");
            });
            log.debug(" Outer end");
        });

        log.debug("Main end");

        worker.dispose();
    }
}
