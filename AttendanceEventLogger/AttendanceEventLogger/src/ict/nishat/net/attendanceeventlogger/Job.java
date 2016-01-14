package ict.nishat.net.attendanceeventlogger;

import java.util.Calendar;

import java.util.EventListener;

import org.quartz.JobExecutionContext;

public class Job implements org.quartz.Job{
    public Job() {
        super();
    }
    
    public void execute(JobExecutionContext jobExecutionContext) {
       /****************************************************
        *          EVENT LOGGING AND LEAVES DEDUCTION      *
        ****************************************************/
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH,0); //14 
        EventLogger evLogger = new EventLogger();
        evLogger.logEvents(date);
    }
    
}
