package se.kth.awsome.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<ApplicationUser, Long>,
        JpaSpecificationExecutor<ApplicationUser> {


    @Query(value = "select U FROM ApplicationUser U WHERE U.email = :theEmail")
    ApplicationUser findByEmail(@Param("theEmail") String email);

    @Query(value = "select U FROM ApplicationUser U WHERE U.id = :userId")
    ApplicationUser findByUserID (@Param("userId") Long userID);

    @Query(value = "select U FROM ApplicationUser U WHERE U.userName = :theUserName")
    ApplicationUser findByUsername (@Param("theUserName") String userName);


}
