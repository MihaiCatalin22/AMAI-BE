package prjcb04.amaiproject2024.config.db;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import prjcb04.amaiproject2024.business.Implementation.AvailableTuesdays;
import prjcb04.amaiproject2024.domain.AvailableTimeslots;
import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.domain.TimeSlot;
import prjcb04.amaiproject2024.persistence.AvailableTimeslotsRepo;
import prjcb04.amaiproject2024.persistence.EventRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {

    private AvailableTimeslotsRepo timeslotRepo;
    private AvailableTuesdays availableTuesdays;
    private List<AvailableTimeslots> timeslots = new ArrayList<>();

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabaseInitialDummyData() {
        var result = availableTuesdays.generate();
        List<AvailableTimeslots> allSlots = AvailableTuesdays.generateTimeSlotsForTuesdays(result);
        if (timeslotRepo.count() == 0) {
            timeslotRepo.saveAll(allSlots);
        }
    }
}
