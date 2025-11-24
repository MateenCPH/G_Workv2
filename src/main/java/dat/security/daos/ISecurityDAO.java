package dat.security.daos;

import dat.entities.User;
import dat.security.exceptions.ValidationException;
import dat.dtos.UserDTO;

public interface ISecurityDAO {
    UserDTO getVerifiedUser(String email, String password) throws ValidationException;
    User createUser(String email, String password);
    User addRole(UserDTO user, String newRole);
}
