package se.kth.awesome.model.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Sys on 2016-11-07.
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long>,
        JpaSpecificationExecutor<UserRole> {

}
