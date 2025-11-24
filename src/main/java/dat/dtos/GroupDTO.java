package dat.dtos;

import dat.entities.Group;
import dat.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private Set<Long> memberIds = new HashSet<>();

    // Constructor from entity
    public GroupDTO(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        if (group.getMembers() != null) {
            this.memberIds = group.getMembers().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
        }
    }

    // Constructor for creating new groups
    public GroupDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
