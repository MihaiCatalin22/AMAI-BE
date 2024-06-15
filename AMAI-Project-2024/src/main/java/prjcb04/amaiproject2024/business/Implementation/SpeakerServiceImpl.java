package prjcb04.amaiproject2024.business.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prjcb04.amaiproject2024.business.SpeakerService;
import prjcb04.amaiproject2024.domain.Speaker;
import prjcb04.amaiproject2024.persistence.SpeakerRepository;

import java.util.List;

@Service
public class SpeakerServiceImpl implements SpeakerService {
    private final SpeakerRepository speakerRepository;

    @Autowired
    public SpeakerServiceImpl(SpeakerRepository speakerRepository) {
        this.speakerRepository = speakerRepository;
    }

    @Override
    public Speaker registerSpeaker(Speaker speaker) {
        return speakerRepository.save(speaker);
    }

    @Override
    public Speaker updateSpeakerDetails(Long speakerId, Speaker speakerDetails) {
        return speakerRepository.findById(speakerId).map(existingSpeaker -> {
            existingSpeaker.setBio(speakerDetails.getBio());
            existingSpeaker.setExpertiseArea(speakerDetails.getExpertiseArea());
            return speakerRepository.save(existingSpeaker);
        }).orElseThrow(() -> new IllegalArgumentException("Speaker not found with id: " + speakerId));
    }

    @Override
    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }

    @Override
    public Speaker getSpeakerById(Long speakerId) {
        return speakerRepository.findById(speakerId)
                .orElseThrow(() -> new IllegalArgumentException("Speaker not found with id: " + speakerId));
    }

    @Override
    public void deleteSpeaker(Long speakerId) {
        speakerRepository.deleteById(speakerId);
    }

}
