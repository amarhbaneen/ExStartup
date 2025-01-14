package com.example.StartupExercise.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing User operations.
 */

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * create a new User
     * @param newUser the new user object to be created
     * @return the created user object
     */
    public User createUser(User newUser){
        return  userRepository.save(newUser);
    }

    /**
     * Retrieves a User by its UserName
     *
     * @param userName the String value represents the username
     * @return the user object if it found
     */

    @Cacheable(value = "User", key = "#userName")
    public User getUserByUserName(String userName){
        return userRepository.findByUsername(userName);
    }
    /**
     * Retrieves a User by its ID
     *
     * @param userId the Integer value represents the ID
     * @return An Optional containing the user object if it found
     */

    @Cacheable(value = "User", key = "#userId")
    public Optional<User> getUserById(Integer userId){
        return userRepository.findById(userId);
    }

    /**
     * Updates an exist user
     * @param ID the ID of the user to update
     * @param UpdatedUser the updated user object
     * @return the updated user object
     * @throws RuntimeException if the user is not found
     *
     */

    @CachePut(value = "User", key = "#ID")
    public User updateUser(Integer ID , User UpdatedUser){
        return userRepository.findById(ID).map(
                user -> {
                    user.setFirstName(UpdatedUser.getFirstName());
                    user.setSurName(UpdatedUser.getSurName());
                    return userRepository.save(user);
                }
        ).orElseThrow(
                () -> new RuntimeException("User not found  with ID :" + ID)
        );
    }
    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users.
     */
   @Cacheable(value = "users",key = "'all_users'")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    /**
     * Delete user using the ID
     * @param ID the ID of the user to be deleted
     * @throws IllegalArgumentException if the user does not exist
     */
    @CacheEvict(value = "User" , key = "#ID")
    public void deleteUser(Integer ID) {
        if (!userRepository.existsById(ID)) {
            throw new IllegalArgumentException("User with ID " + ID + " does not exist.");
        }
        userRepository.deleteById(ID);
    }
}
