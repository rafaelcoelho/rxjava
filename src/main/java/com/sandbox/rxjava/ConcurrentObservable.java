package com.sandbox.rxjava;

import static com.sandbox.rxjava.util.Utils.sleepOneSecond;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConcurrentObservable
{

    public static void main(String[] args)
    {
        final Single<BigDecimal> single = Observable.just("Apple", "Banana", "Milk", "Tomato", "Pineapple", "Bread")
                .flatMap(it -> purchase(it, 1).subscribeOn(Schedulers.io()))
                .reduce(BigDecimal::add)
                .toSingle();

        final long timeBefore = System.currentTimeMillis();
        single.blockingSubscribe();
        log.debug("Executed in {} [ms]", System.currentTimeMillis() - timeBefore);

        log.debug("Exiting");
    }

    private static Observable<BigDecimal> purchase(final String name, final int qtde)
    {
        return Observable.fromCallable(() -> doPurchase(name, qtde));
    }

    private static BigDecimal doPurchase(final String name, final int qtde)
    {
        log.debug("Purchasing {} + {}", name, qtde);

        sleepOneSecond();
        sleepOneSecond();
        sleepOneSecond();

        log.debug("Done ${} for {}", qtde * 5, name);

        return BigDecimal.valueOf(qtde * 5);
    }

}
