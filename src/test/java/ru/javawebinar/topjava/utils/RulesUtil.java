package ru.javawebinar.topjava.utils;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesUtil {

    private static final Logger log = LoggerFactory.getLogger(RulesUtil.class);
    private static StringBuilder result = new StringBuilder();

    public static final Stopwatch STOP_WATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            log.info("Time: {}", nanos);
            result.append("Name: ").append(description.getMethodName()).append(" time: ").append(nanos).append('\n');
        }
    };

    public static final ExternalResource EXTERNAL_RESOURCE = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            result = new StringBuilder();
        }

        @Override
        protected void after() {
            System.out.println(result);
        }
    };
}
