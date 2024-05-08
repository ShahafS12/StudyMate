package com.studymate.repositories;

import com.studymate.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    Group findByGroupName(@Param("groupName") String groupName);
    void deleteByGroupName(String groupName);


}
