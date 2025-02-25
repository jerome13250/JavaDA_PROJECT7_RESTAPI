package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
	
	User findByUsername(String username);
	
	@Query("SELECT CASE "
			+ "WHEN COUNT(u) > 0 THEN true"
			+ " ELSE false END "
			+ "FROM User u "
			+ "WHERE u.username = :username")
    public Boolean existsByUsername(@Param("username") String email);

}
