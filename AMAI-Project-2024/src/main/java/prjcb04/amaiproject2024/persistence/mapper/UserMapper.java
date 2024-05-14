package prjcb04.amaiproject2024.persistence.mapper;

import org.mapstruct.*;
import prjcb04.amaiproject2024.business.dto.UserDTO;
import prjcb04.amaiproject2024.domain.User;
import prjcb04.amaiproject2024.domain.Role;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "jwt", ignore = true)
    UserDTO toDto(User user);

    @AfterMapping
    default void convertRolesToStrings(User user, @MappingTarget UserDTO dto) {
        List<String> roles = user.getRoles()
                .stream()
                .map(Enum::name)
                .toList();
        dto.setRole(roles);
    }


    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(UserDTO userDTO);

    @AfterMapping
    default void handleRoleConversion(@MappingTarget User user, UserDTO userDTO) {
        if (userDTO.getRole() != null) {
            user.setRoles(userDTO.getRole().stream()
                    .map(String::toUpperCase)
                    .map(Role::valueOf)
                    .toList());
        }
    }
}