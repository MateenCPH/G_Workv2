package dat.dtos;

import dat.entities.Group;
import dat.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GroupDTO {
    private int id;
    private String name;
    private String description;
    private Set<Integer> memberIds = new HashSet<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupDTO groupDTO = (GroupDTO) o;
        return Objects.equals(id, groupDTO.id) && 
               Objects.equals(name, groupDTO.name) && 
               Objects.equals(description, groupDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "GroupDTO(id=" + id + ", name=" + name + ", description=" + description + 
               ", memberIds=" + memberIds + ")";
    }
}
