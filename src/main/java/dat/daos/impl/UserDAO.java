package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.UserDTO;
import dat.entities.User;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserDAO implements IDAO<UserDTO, Integer> {

    private static UserDAO instance;
    private static EntityManagerFactory emf;

    public static UserDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public UserDTO readById(Integer id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, id);
            if (user == null) {
                throw new ApiException(404, "User not found");
            }
            return new UserDTO(user);
        }
    }

    public UserDTO readByEmail(String email) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            if (user == null) {
                throw new ApiException(404, "User not found");
        }
            return new UserDTO(user);
        }
    }

    public UserDTO readByUsername(String username) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            if (user == null) {
                throw new ApiException(404, "User not found");
            }
            return new UserDTO(user);
        }
    }

    //Method to read user by first name and get results as a list
    public List<UserDTO> readByFirstName(String firstName) {
        try (EntityManager em = emf.createEntityManager()) {
            List<User> users = em.createQuery("SELECT u FROM User u WHERE u.firstName = :firstName", User.class)
                    .setParameter("firstName", firstName)
                    .getResultList();
            return users.stream().map(UserDTO::new).toList();
        }
    }

    @Override
    public List<UserDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            return users.stream().map(UserDTO::new).toList();
        }
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        return null; //SecurityDAO has createUser method
    }

    @Override
    public UserDTO update(Integer id, UserDTO userDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, id);

            if (user == null) {
                throw new ApiException(404, "User not found");
            }

            // Update fields if provided
            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                user.setEmail(userDTO.getEmail());
            }

            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                user.setEmail(userDTO.getEmail());
            }

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt()));
            }

            if (userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty()) {
                user.setFirstName(userDTO.getFirstName());
            }

            if (userDTO.getLastName() != null && !userDTO.getLastName().isEmpty()) {
                user.setLastName(userDTO.getLastName());
            }

            // Activate/deactivate user if active state is provided
            if (userDTO.isActive() != user.isActive()) {
                user.setActive(userDTO.isActive());
            }

            em.merge(user);
            em.getTransaction().commit();

            return new  UserDTO(user);
        } catch (Exception e) {
            throw new ApiException(500, "Error updating user: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer integer) throws ApiException {
        return;
    }
}
