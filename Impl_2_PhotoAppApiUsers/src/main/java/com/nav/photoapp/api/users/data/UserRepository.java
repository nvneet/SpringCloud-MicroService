package com.nav.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;

// Creating CrudRepository instead of DAO layer
public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);
}
