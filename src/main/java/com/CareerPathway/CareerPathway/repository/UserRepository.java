package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
    List<User> findAllByRole(Role role);

}
