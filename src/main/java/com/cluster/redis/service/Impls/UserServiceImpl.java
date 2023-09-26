package com.cluster.redis.service.Impls;

import com.cluster.redis.pojo.User;
import com.cluster.redis.publisher.RedisPublisher;
import com.cluster.redis.repository.UserRepository;
import com.cluster.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    RedisPublisher redisPublisher;

    @Override
    public boolean saveUser(User user) {
        boolean result = userRepository.saveUser(user);
        if (result){
            redisPublisher.publish("smtp",
                    "New User Saved:- id: "+user.getId()+ " and email: "+user.getEmail());
        }
        return result;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }

    @Override
    public boolean updateUser(Long id, User user) {
        return userRepository.updateUser(id, user);
    }
}
