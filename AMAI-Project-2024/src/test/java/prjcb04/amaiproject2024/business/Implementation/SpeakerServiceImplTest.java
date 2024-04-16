package prjcb04.amaiproject2024.business.Implementation;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prjcb04.amaiproject2024.domain.Speaker;
import prjcb04.amaiproject2024.persistence.SpeakerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SpeakerServiceImplTest {

    @Mock
    private SpeakerRepository speakerRepository;

    @InjectMocks
    private SpeakerServiceImpl speakerService;

    private Speaker speaker;

    @BeforeEach
    void setUp() {
        speaker = new Speaker();
        speaker.setId(1L);
        speaker.setBio("Experienced AI researcher");
        speaker.setExpertiseArea("Artificial Intelligence");
    }

    @Test
    void registerSpeaker_HappyPath() {
        when(speakerRepository.save(any(Speaker.class))).thenReturn(speaker);
        Speaker registeredSpeaker = speakerService.registerSpeaker(speaker);
        assertNotNull(registeredSpeaker);
        verify(speakerRepository).save(speaker);
    }

    @Test
    void updateSpeakerDetails_HappyPath() {
        when(speakerRepository.findById(anyLong())).thenReturn(Optional.of(speaker));
        when(speakerRepository.save(any(Speaker.class))).thenReturn(speaker);
        Speaker updatedSpeaker = speakerService.updateSpeakerDetails(1L, speaker);
        assertNotNull(updatedSpeaker);
        assertEquals("Artificial Intelligence", updatedSpeaker.getExpertiseArea());
        verify(speakerRepository).save(speaker);
    }

    @Test
    void updateSpeakerDetails_NotFound() {
        when(speakerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> speakerService.updateSpeakerDetails(1L, speaker));
    }

    @Test
    void getAllSpeakers() {
        when(speakerRepository.findAll()).thenReturn(Arrays.asList(speaker));
        List<Speaker> speakers = speakerService.getAllSpeakers();
        assertFalse(speakers.isEmpty());
        assertEquals(1, speakers.size());
        verify(speakerRepository).findAll();
    }

    @Test
    void getSpeakerById_HappyPath() {
        when(speakerRepository.findById(anyLong())).thenReturn(Optional.of(speaker));
        Speaker foundSpeaker = speakerService.getSpeakerById(1L);
        assertNotNull(foundSpeaker);
        assertEquals(1L, foundSpeaker.getId());
    }

    @Test
    void getSpeakerById_NotFound() {
        when(speakerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> speakerService.getSpeakerById(1L));
    }

    @Test
    void deleteSpeaker() {
        doNothing().when(speakerRepository).deleteById(anyLong());
        speakerService.deleteSpeaker(1L);
        verify(speakerRepository).deleteById(1L);
    }
}