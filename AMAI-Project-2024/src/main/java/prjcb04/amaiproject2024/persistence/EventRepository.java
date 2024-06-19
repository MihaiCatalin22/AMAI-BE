package prjcb04.amaiproject2024.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTopicContainingIgnoreCase(String topic);

    @Query("SELECT e FROM Event e WHERE e.date BETWEEN :startOfTimeSlot AND :endOfTimeSlot")
    List<Event> findByDateTimeSlot(LocalDateTime startOfTimeSlot, LocalDateTime endOfTimeSlot);

    List<Event> findBySpeakerId(Long speakerId);

    @Query("SELECT e FROM Event e WHERE e.speaker.id IN :speakerIds")
    List<Event> findBySpeakerIds(@Param("speakerIds") List<Long> speakerIds);

    @Query("SELECT e FROM Event e WHERE e.topic LIKE %:topic% AND e.speaker.id IN :speakerIds")
    List<Event> findByTopicAndSpeakerIds(@Param("topic") String topic, @Param("speakerIds") List<Long> speakerIds);
    List<Event> findBySpeaker(User speaker);

}