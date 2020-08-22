package com.sandbox.rxjava;

import static com.sandbox.rxjava.util.Utils.sleepOneSecond;

import java.math.BigDecimal;

import org.apache.commons.lang3.tuple.Pair;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ObservableGroupBy
{
    public static void main(String[] args)
    {
        final Single<BigDecimal> totalPrice = Observable.just("bread", "juice", "juice", "bread", "juice", "juice", "bread", "juice", "apple")
                .groupBy(it -> it)
                .flatMap(grouped -> grouped
                        .count()
                        .map(qtde -> Pair.of(grouped.getKey(), qtde))
                        .toObservable())
                .flatMap(order -> ObservableGroupBy.purchase(order.getKey(), order.getValue())
                        .subscribeOn(Schedulers.io()))
                .reduce(BigDecimal::add)
                .toSingle();

        final long timeBefore = System.currentTimeMillis();
        totalPrice.blockingSubscribe(log::debug, Throwable::printStackTrace);
        log.debug("Executed in {} [ms]", System.currentTimeMillis() - timeBefore);
    }

    private static Observable<BigDecimal> purchase(final String name, final Long qtde)
    {
        return Observable.fromCallable(() -> doPurchase(name, qtde));
    }

    private static BigDecimal doPurchase(final String name, final Long qtde)
    {
        log.debug("Purchasing {} + {}", name, qtde);
        sleepOneSecond();
        log.debug("Done ${} for {}", qtde * 10, name);

        return BigDecimal.valueOf(qtde * 10);
    }
}
