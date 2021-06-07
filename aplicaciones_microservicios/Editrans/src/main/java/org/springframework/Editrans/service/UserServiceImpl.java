package org.springframework.Editrans.service;

import org.springframework.Editrans.model.User;
import org.springframework.Editrans.repository.RoleRepository;
import org.springframework.Editrans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @SuppressWarnings("null")
	@KafkaListener(topics = "${message.topic.name}", groupId = "${message.group.name")
    public void listenTopic1(String message) {
        System.out.println("Recieved Message of topic1 in  listener: " + message);
        User user = new User();
        user.setUsername(message);
        user.setPassword(bCryptPasswordEncoder.encode(message));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
        
    }
}
