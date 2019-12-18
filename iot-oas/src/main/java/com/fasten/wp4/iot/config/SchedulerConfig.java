package com.fasten.wp4.iot.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    
	@Value("${executor.pool.size:20}")
	private int poolSize;
	
	@Value("${executor.name.prefix:kafkaProducer}")
	private String namePrefix;
	
	private ScheduledTaskRegistrar taskRegistrar;
	
	
	@Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(poolSize, new CustomizableThreadFactory(namePrefix));
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        setTaskRegistrar(taskRegistrar);
    }

	public ScheduledTaskRegistrar getTaskRegistrar() {
		return taskRegistrar;
	}

	public void setTaskRegistrar(ScheduledTaskRegistrar taskRegistrar) {
		this.taskRegistrar = taskRegistrar;
	}
    
}

//        taskRegistrar.addTriggerTask(
//                new Runnable() {
//                    @Override public void run() {
//                        myBean().getSchedule();
//                    }
//                },
//                new Trigger() {
//                    @Override public Date nextExecutionTime(TriggerContext triggerContext) {
//                        Calendar nextExecutionTime =  new GregorianCalendar();
//                        Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
//                        nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
//                        nextExecutionTime.add(Calendar.MILLISECOND, env.getProperty("myRate", Integer.class)); //you can get the value from wherever you want
//                        return nextExecutionTime.getTime();
//                    }
//                }
//        );