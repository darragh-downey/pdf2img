package com.ddowney.plugins.pdf2img.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

//Example of quartz scheduled job running
public class HelloWorldJob {

	public static void main(String[] args) throws SchedulerException {
		// TODO Auto-generated method stub
		JobDetail job = JobBuilder.newJob(ThumbGen.class).withIdentity("dummyJobName", "group1").build();
		
		JobDetail j = JobBuilder.newJob(GenJob.class).withIdentity("GenJob", "genjobGroup").build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("dummyTriggerName", "group1")
				.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
				.build();
		
		Trigger t = TriggerBuilder.newTrigger()
					.withIdentity("GenJob", "genjobGroup")
					.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
					.build();
		
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		Scheduler s = new StdSchedulerFactory().getScheduler();
		
		scheduler.start();
		s.start();
		
		scheduler.scheduleJob(job, trigger);
		s.scheduleJob(j, t);
	}

}
