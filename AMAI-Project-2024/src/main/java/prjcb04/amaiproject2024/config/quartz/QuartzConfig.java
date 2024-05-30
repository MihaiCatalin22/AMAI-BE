package prjcb04.amaiproject2024.config.quartz;


import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import prjcb04.amaiproject2024.business.Jobs.SendInvitesFirstPeriod;
import prjcb04.amaiproject2024.business.Jobs.SendInvitesSecondPeriod;

@Configuration
public class QuartzConfig {
    @Autowired
    private AutowiringSpringBeanJobFactory jobFactory;
    @Bean
    public JobDetail jobOneDetail() {
        return JobBuilder.newJob(SendInvitesFirstPeriod.class)
                .withIdentity("jobOne")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobOneTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobOneDetail())
                .withIdentity("jobOneTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 1 9 ? *"))
                .build();
    }

    @Bean
    public JobDetail jobTwoDetail() {
        return JobBuilder.newJob(SendInvitesSecondPeriod.class)
                .withIdentity("jobTwo")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTwoTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobTwoDetail())
                .withIdentity("jobTwoTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 19 2 ? *"))
                .build();
    }

    @Bean
    public Trigger jobThreeTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobTwoDetail())
                .withIdentity("jobThreeTrigger")
                //.withSchedule(CronScheduleBuilder.cronSchedule("00 30 10 6 6 ? *"))
                .withSchedule(CronScheduleBuilder.cronSchedule("0 46 12 29 5 ? *"))
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger jobOneTrigger, Trigger jobTwoTrigger,Trigger jobThreeTrigger, JobDetail jobOneDetail, JobDetail jobTwoDetail) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobFactory(jobFactory);
        schedulerFactory.setJobDetails(jobOneDetail, jobTwoDetail);
        schedulerFactory.setTriggers(jobOneTrigger, jobTwoTrigger,jobThreeTrigger);
        return schedulerFactory;
    }
}
