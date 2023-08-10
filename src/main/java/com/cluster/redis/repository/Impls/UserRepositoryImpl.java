package com.cluster.redis.repository.Impls;

import com.cluster.redis.pojo.User;
import com.cluster.redis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String REDIS_HASH_KEY = "USERS";

    @Autowired
    private HashOperations<String, String, User> hashOps;

    @Override
    public boolean saveUser(User user) {
        try {
            hashOps.put(REDIS_HASH_KEY, user.getId().toString(), user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = hashOps.values(REDIS_HASH_KEY);
        return users;
    }

    @Override
    public User getUserById(Long id) {
        User user = (User) hashOps.get(REDIS_HASH_KEY, id.toString());
        return user;
    }

    @Override
    public boolean deleteUser(Long id) {
        try {
            hashOps.delete(REDIS_HASH_KEY, id.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(Long id, User user) {
        try {
            hashOps.put(REDIS_HASH_KEY, id.toString(), user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
