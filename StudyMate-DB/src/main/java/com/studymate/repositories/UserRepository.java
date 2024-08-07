package com.studymate.repositories;

import com.studymate.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUserName(@Param("username") String username);
    List<User> findMembersByUserName(@Param("username") List<String> members);
    @Query(value = "{}", fields = "{'userName': 1, '_id': 0}")
    List<String> findAllUserNames();
    List<User> findByUserNameContainingIgnoreCase(String userName);
    User findByEmail(@Param("email") String email);
    User findByUserNameAndPassword(@Param("username") String username, @Param("password") String password);
    void deleteByUserName(@Param("username") String username);

}
