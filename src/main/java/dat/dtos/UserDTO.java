package dat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import dat.entities.User;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private int id;
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean active;
    private Set<String> roles = new HashSet<>();

    // Constructor from User entity
    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.active = user.isActive();
        this.roles = user.getRolesAsStrings();
    }

    public UserDTO(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO(String email, Set<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public UserDTO(int id, String email, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            UserDTO dto = (UserDTO) o;
            return Objects.equals(this.email, dto.email) && Objects.equals(this.roles, dto.roles);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.email, this.roles});
    }

    public String toString() {
        return "UserDTO(id=" + this.getId() + ", email=" + this.getEmail() + ", password=" + this.password + ", roles=" + this.getRoles() + ")";
    }
}
