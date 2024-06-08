package prjcb04.amaiproject2024.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubscribeToCalendarRequest {
    Long userId;
    Boolean subscribes;
}
