package prjcb04.amaiproject2024.business.Jobs;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import prjcb04.amaiproject2024.business.Implementation.EmailSender;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class SendInvitesSecondPeriod implements Job {
    @Autowired
    EmailSender emailSender;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        emailSender.sendInvitesSecondPeriod();
    }
}
