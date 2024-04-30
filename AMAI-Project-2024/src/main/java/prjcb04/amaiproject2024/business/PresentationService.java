package prjcb04.amaiproject2024.business;

import prjcb04.amaiproject2024.domain.Presentation;
import java.util.List;
import java.util.Optional;
public interface PresentationService {
    Presentation createPresentation(Presentation presentation);
    Optional<Presentation> getPresentationById(Long id);
    List<Presentation> getAllPresentations();
    Presentation updatePresentation(Long id, Presentation presentation);
    void deletePresentation(Long id);

}
