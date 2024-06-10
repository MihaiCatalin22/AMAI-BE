package prjcb04.amaiproject2024.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import prjcb04.amaiproject2024.domain.AvailableTimeslots;

import java.time.LocalDate;
import java.util.List;

public interface AvailableTimeslotsRepo extends JpaRepository<AvailableTimeslots,Long> {
    List<AvailableTimeslots> findByDate(LocalDate date);
}
