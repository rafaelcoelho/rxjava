package com.sandbox.rxjava;

import static com.sandbox.rxjava.util.Utils.sleepOneSecond;

import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ObservableExample
{
    public static void main(String[] args)
    {
        log.debug("Starting");

        final Observable<String> obs = simple();
        log.debug("Created");

        final Observable<String> obs2 = obs
                .map(it -> it)
                .doOnNext(it -> log.debug("Mapping {}", it))
                .filter(Objects::nonNull)
                .doOnNext(it -> log.debug("Filtering {}", it));

        log.debug("Modified");

        final Observable<String> just = Observable.just("1", "2");

        obs2.subscribe(
                log::debug,
                Throwable::printStackTrace,
                () -> log.debug("Completed"));

        sleepOneSecond();
        sleepOneSecond();
        sleepOneSecond();
        sleepOneSecond();

        log.debug("Exiting");
    }

    private static Observable<String> simple()
    {
        return Observable.create(subs -> {
            log.debug("Subscribed");

            log.debug("Pumping A");
            subs.onNext("A");

            log.debug("Pumping B");
            subs.onNext("B");

            log.debug("Completing");
            subs.onComplete();
        });
    }
}
