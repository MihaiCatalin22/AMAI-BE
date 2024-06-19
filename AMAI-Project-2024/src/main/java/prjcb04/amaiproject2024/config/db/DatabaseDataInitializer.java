package prjcb04.amaiproject2024.config.db;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import prjcb04.amaiproject2024.business.Implementation.AvailableTuesdays;
import prjcb04.amaiproject2024.domain.AvailableTimeslots;
import prjcb04.amaiproject2024.persistence.AvailableTimeslotsRepo;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {
    private final AvailableTimeslotsRepo timeslotRepo;
    private final AvailableTuesdays availableTuesdays;

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabaseInitialDummyData() {
        if (timeslotRepo.count() == 0) {
            List<LocalDate> tuesdays = availableTuesdays.generate();
            List<AvailableTimeslots> allSlots = AvailableTuesdays.generateTimeSlotsForTuesdays(tuesdays);
            timeslotRepo.saveAll(allSlots);
        } else {
            System.out.println("Database already initialized. Skipping timeslot initialization.");
        }
    }
}
