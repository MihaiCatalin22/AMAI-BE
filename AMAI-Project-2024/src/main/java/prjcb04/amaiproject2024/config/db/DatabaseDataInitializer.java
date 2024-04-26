package prjcb04.amaiproject2024.config.db;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import prjcb04.amaiproject2024.domain.Event;
import prjcb04.amaiproject2024.persistence.EventRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {

    private EventRepository eventRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabaseInitialDummyData() {

        if (eventRepository.count() == 0) {
            //upcoming
            eventRepository.save(Event.builder()
                    .topic("Introduction Fontys Research Data Management")
                    .description("Introduction to React fundamentals") // removed extra parentheses
                    .speakers(new ArrayList<>(Arrays.asList("Vera Timmers-Haagsma", "Frank de Nijs")))
                    .date(LocalDateTime.of(2024, Month.APRIL,2,18,30,0))
                    .build());

            eventRepository.save(Event.builder()
                    .topic("VVT project update")
                    .description("Managing state in React applications") // removed extra parentheses
                    .speakers(new ArrayList<>(Arrays.asList("Joris Geurts")))
                    .date(LocalDateTime.of(2024, Month.APRIL,9,18,0,0))
                    .build());

            //past
            eventRepository.save(Event.builder()
                    .topic("Data and Nature in the City")
                    .description("I will explain why Fontys ICT needs a new research line where data is used to support decision-making about urban nature. What does this mean exactly, why is it interesting for us, what happens already in this direction, how will we finance it? This is a cooperation with Naturalis Biodiversity Center and other inspiring partners.") // removed extra parentheses
                    .speakers(new ArrayList<>(Arrays.asList("Simona Orzan")))
                    .date(LocalDateTime.of(2024, Month.MARCH,5,18,0,0))
                    .build());
            eventRepository.save(Event.builder()
                    .topic("Generative AI in Education")
                    .description("") // removed extra parentheses
                    .speakers(new ArrayList<>(Arrays.asList("Erdinc Sacan")))
                    .date(LocalDateTime.of(2024, Month.JANUARY,23,18,0,0))
                    .build());
            eventRepository.save(Event.builder()
                    .topic("AI fusion models for the Flower Power project")
                    .description("") // removed extra parentheses
                    .speakers(new ArrayList<>(Arrays.asList("Georgiana Manolache")))
                    .date(LocalDateTime.of(2024, Month.FEBRUARY,21,18,0,0))
                    .build());
            eventRepository.save(Event.builder()
                    .topic("DEMAND Project Update")
                    .description("") // removed extra parentheses
                    .speakers(new ArrayList<>(Arrays.asList("Leon Schrijvers")))
                    .date(LocalDateTime.of(2024, Month.JANUARY,23,18,0,0))
                    .build());
            eventRepository.save(Event.builder()
                    .topic("Quality of AI Systems (results of case studies & how to implement S6, Minor AI)")
                    .description("") // removed extra parentheses
                    .speakers(new ArrayList<>(Arrays.asList("Merel Veracx")))
                    .date(LocalDateTime.of(2023, Month.DECEMBER,19,18,0,0))
                    .build());
        }
    }
}
