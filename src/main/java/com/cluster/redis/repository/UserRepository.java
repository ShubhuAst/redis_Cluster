package com.cluster.redis.repository;

import com.cluster.redis.pojo.User;

import java.util.List;

public interface UserRepository {

    boolean saveUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    boolean deleteUser(Long id);

    boolean updateUser(Long id, User user);

}
