package com.studymate.apis.Mongo.Repositories;

import com.studymate.apis.Mongo.Document.UserDoc;
import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserDoc, String> {
    UserDoc findByUsername(@NonNull String username);
    UserDoc findByEmail(@NonNull String email);
    UserDoc findByUsernameAndPassword(@NonNull String username, @NonNull String password);

    void deleteByUsername(@NonNull String username);
}
