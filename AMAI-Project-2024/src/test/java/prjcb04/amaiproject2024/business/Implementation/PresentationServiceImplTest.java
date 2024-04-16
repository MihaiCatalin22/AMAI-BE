package prjcb04.amaiproject2024.business.Implementation;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prjcb04.amaiproject2024.domain.Presentation;
import prjcb04.amaiproject2024.persistence.PresentationRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PresentationServiceImplTest {

    @Mock
    private PresentationRepository presentationRepository;

    @InjectMocks
    private PresentationServiceImpl presentationService;

    private Presentation presentation;

    @BeforeEach
    void setUp() {
        presentation = new Presentation();
        presentation.setId(1L);
        presentation.setTitle("AI Advances");
        presentation.setDescription("Discussion on recent AI advancements");
        presentation.setPresentationDate(LocalDateTime.now());
    }

    @Test
    void createPresentation_HappyPath() {
        when(presentationRepository.save(any(Presentation.class))).thenReturn(presentation);
        Presentation createdPresentation = presentationService.createPresentation(presentation);
        assertNotNull(createdPresentation);
        verify(presentationRepository).save(presentation);
    }

    @Test
    void getPresentationById_HappyPath() {
        when(presentationRepository.findById(anyLong())).thenReturn(Optional.of(presentation));
        Optional<Presentation> foundPresentation = presentationService.getPresentationById(1L);
        assertTrue(foundPresentation.isPresent());
        assertEquals(presentation.getId(), foundPresentation.get().getId());
    }

    @Test
    void getPresentationById_NotFound() {
        when(presentationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<Presentation> foundPresentation = presentationService.getPresentationById(1L);
        assertFalse(foundPresentation.isPresent());
    }

    @Test
    void getAllPresentations() {
        when(presentationRepository.findAll()).thenReturn(Arrays.asList(presentation));
        List<Presentation> presentations = presentationService.getAllPresentations();
        assertFalse(presentations.isEmpty());
        assertEquals(1, presentations.size());
        verify(presentationRepository).findAll();
    }

    @Test
    void updatePresentation_HappyPath() {
        Presentation updatedDetails = new Presentation();
        updatedDetails.setTitle("AI Ethics");
        updatedDetails.setDescription("Exploring ethical concerns in AI");

        when(presentationRepository.findById(anyLong())).thenReturn(Optional.of(presentation));
        when(presentationRepository.save(any(Presentation.class))).thenReturn(presentation);

        Presentation updatedPresentation = presentationService.updatePresentation(1L, updatedDetails);
        assertNotNull(updatedPresentation);
        assertEquals("AI Ethics", updatedPresentation.getTitle());
        verify(presentationRepository).save(presentation);
    }

    @Test
    void updatePresentation_NotFound() {
        when(presentationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> presentationService.updatePresentation(1L, new Presentation()));
    }

    @Test
    void deletePresentation() {
        doNothing().when(presentationRepository).deleteById(anyLong());
        presentationService.deletePresentation(1L);
        verify(presentationRepository).deleteById(1L);
    }
}