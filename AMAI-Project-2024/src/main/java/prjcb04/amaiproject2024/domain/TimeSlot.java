package prjcb04.amaiproject2024.domain;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TimeSlot {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }
}
