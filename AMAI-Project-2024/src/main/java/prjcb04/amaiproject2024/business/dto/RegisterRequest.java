package prjcb04.amaiproject2024.business.dto;

import lombok.*;
import prjcb04.amaiproject2024.domain.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    User user;
    String url;
}
