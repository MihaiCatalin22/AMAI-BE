package prjcb04.amaiproject2024.business.Implementation;

import prjcb04.amaiproject2024.business.PresentationService;
import prjcb04.amaiproject2024.domain.Presentation;
import prjcb04.amaiproject2024.persistence.PresentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PresentationServiceImpl implements PresentationService {

    private final PresentationRepository presentationRepository;

    @Autowired
    public PresentationServiceImpl(PresentationRepository presentationRepository) {
        this.presentationRepository = presentationRepository;
    }

    @Override
    public Presentation createPresentation(Presentation presentation) {
        return presentationRepository.save(presentation);
    }

    @Override
    public Optional<Presentation> getPresentationById(Long id) {
        return presentationRepository.findById(id);
    }

    @Override
    public List<Presentation> getAllPresentations() {
        return presentationRepository.findAll();
    }

    @Override
    public Presentation updatePresentation(Long id, Presentation updatedPresentation) {
        return presentationRepository.findById(id)
                .map(presentation -> {
                    presentation.setTitle(updatedPresentation.getTitle());
                    presentation.setDescription(updatedPresentation.getDescription());
                    presentation.setPresentationDate(updatedPresentation.getPresentationDate());
                    return presentationRepository.save(presentation);
                }).orElseThrow(() -> new IllegalArgumentException("Presentation not found with id: " + id));
    }

    @Override
    public void deletePresentation(Long id) {
        presentationRepository.deleteById(id);
    }
}
