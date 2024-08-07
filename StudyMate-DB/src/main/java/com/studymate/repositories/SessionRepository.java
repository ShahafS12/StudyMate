package com.studymate.repositories;


import com.studymate.model.Session.Session;
import com.studymate.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface SessionRepository extends MongoRepository<Session, ObjectId> {
      Session findBySessionId(@Param("id") String id);
}
