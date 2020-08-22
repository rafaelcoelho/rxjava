package com.sandbox.rxjava;

import static io.netty.util.CharsetUtil.UTF_8;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ReactiveTcpServer
{
    public static final BigDecimal RATE = new BigDecimal("1.06448");

    public static void main(String[] args)
    {
        TcpServer
                .newServer(8080)
                .<String, String>pipelineConfigurator(pipeline -> {
                    pipeline.addLast(new LineBasedFrameDecoder(1024));
                    pipeline.addLast(new StringDecoder(UTF_8));
                })
                .start(connection -> {
                    final rx.Observable<String> result = connection
                            .getInput()
                            .autoRelease()
                            .doOnNext(log::debug)
                            .map(BigDecimal::new)
                            .flatMap(ReactiveTcpServer::convert);

                    return connection.writeAndFlushOnEach(result);
                })
                .awaitShutdown();
    }

    private static rx.Observable<String> convert(final BigDecimal value)
    {
        return rx.Observable
                .just(value.multiply(RATE))
                .map(amount -> value + " EUR is " + amount + " USD\n")
                .doOnNext(it -> log.debug("Mapped to {}", it))
                .delay(1, TimeUnit.SECONDS);
    }
}
