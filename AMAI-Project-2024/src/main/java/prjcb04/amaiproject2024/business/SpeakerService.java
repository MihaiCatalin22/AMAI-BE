package prjcb04.amaiproject2024.business;

import prjcb04.amaiproject2024.domain.Speaker;

import java.util.List;
public interface SpeakerService {
    Speaker registerSpeaker(Speaker speaker);
    Speaker updateSpeakerDetails(Long speakerId, Speaker speakerDetails);
    List<Speaker> getAllSpeakers();
    Speaker getSpeakerById(Long speakerId);
    void deleteSpeaker(Long speakerId);

}
