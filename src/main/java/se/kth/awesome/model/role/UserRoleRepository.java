package se.kth.awesome.model.role;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sys on 2016-11-07.
 */
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long>,
        JpaSpecificationExecutor<UserRoleEntity> {

//	@Query(value = "SELECT A FROM RolesEntity A where A.role = :role")
//	RolesEntity findOneByUserRole(@Param("role") String userRole);

//	@Query(value = "SELECT UA.pk.role FROM UserRoleEntity UA where UA.pk.user.id = :userId")
//	List<RolesEntity> findAuthorityByUserId(@Param("userId") Long user_id);
//
//	@Modifying
//	@Transactional
//	@Query(value = "delete from UserRoleEntity UA where UA.pk.user.id = :userId1" )
//	void deleteUserAuthorityByUserID(@Param("userId1") Long userID1);

}
