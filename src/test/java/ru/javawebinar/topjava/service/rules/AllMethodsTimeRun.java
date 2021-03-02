package ru.javawebinar.topjava.service.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllMethodsTimeRun implements TestRule {
    private static final Logger LOG = LoggerFactory.getLogger(AllMethodsTimeRun.class);
    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                statement.evaluate();
                LOG.info("Return timing list all methods");
                MethodTimeRun.testTime.forEach(System.out::println);
            }

        };
    }
}