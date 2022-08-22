package com.shah.bankingapplicationcrud.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.redhogs.cronparser.CronExpressionDescriptor.getDescription;

@Slf4j
@Configuration
@EnableScheduling
public class MyScheduler {

    @Value("${scheduled.period.in-cron}")
    private String scheduledPeriodInCron;

    @Scheduled(cron = "${scheduled.period.in-cron}")
    public void scheduler() throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateTimeFormatted = dateFormat.format(date);

        String interval = getDescription(scheduledPeriodInCron);

        log.info("Scheduler triggered for " + interval + " on " + dateTimeFormatted);
    }
}
