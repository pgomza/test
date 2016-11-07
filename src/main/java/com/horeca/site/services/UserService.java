package com.horeca.site.services;

import com.horeca.site.models.user.User;
import com.horeca.site.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository repository;

    public Set<User> getAll() {
        Set<User> users = new HashSet<>();
        for (User user : repository.findAll()) {
            users.add(user);
        }
        return users;
    }

    public User get(Long id) {
        return repository.findOne(id);
    }

    public User save(@Valid User entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
