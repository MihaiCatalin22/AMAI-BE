package prjcb04.amaiproject2024.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prjcb04.amaiproject2024.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTopicContainingIgnoreCase(String topic);

    @Query("SELECT e FROM Event e WHERE e.date BETWEEN :startOfTimeSlot AND :endOfTimeSlot")
    List<Event> findByDateTimeSlot(LocalDateTime startOfTimeSlot, LocalDateTime endOfTimeSlot);
    @Query("SELECT DISTINCT e FROM Event e " +
            "JOIN e.speakers es " +
            "WHERE LOWER(es.speakers) LIKE LOWER(CONCAT('%', :speakers, '%'))")
    List<Event> findBySpeakersContainingIgnoreCase(@Param("speakers")String speakers);
}
