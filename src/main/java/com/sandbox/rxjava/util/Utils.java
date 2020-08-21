package com.sandbox.rxjava.util;

import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class Utils
{
    public static void sleepOneSecond()
    {
        try
        {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (InterruptedException e)
        {
            log.error("Exception to sleep ", e);
        }
    }
}
