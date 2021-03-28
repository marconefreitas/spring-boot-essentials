package academy.devdojo.springboot.repository;

import academy.devdojo.springboot.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {

    DevDojoUser findByUsername(String userName);



}
