package com.academy.fourtk.springbootessentials.repositories;

import com.academy.fourtk.springbootessentials.entities.DevUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevUserRepository extends JpaRepository<DevUser, Long> {
    DevUser findByUsername(String username);
}
