package com.studymate.apis.Mongo.Repositories;

import Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUserName(@Param("username") String username);
    User findByEmail(@Param("email") String email);
    User findByUserNameAndPassword(@Param("username") String username, @Param("password") String password);

    void deleteByUserName(@Param("username") String username);
}
