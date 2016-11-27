package se.kth.awesome.model.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoleEntityRepository extends JpaRepository<RolesEntity, Long>, JpaSpecificationExecutor<RolesEntity> {

	@Query(value = "SELECT A.authority FROM UserRoleEntity A where A.authority.authority = :role")
	RolesEntity findOneByUserRole(@Param("role") String userRole);

//	@Query(value = "SELECT UA.authority FROM UserRoleEntity UA where UA.a = :userId")
//	List<RolesEntity> findAuthorityByUserId(@Param("userId") Long user_id);

}
