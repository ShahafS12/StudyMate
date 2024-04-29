package com.studymate.apis.Mongo.Repositories;

import Model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    Group findByGroupName(@Param("group_name") String group_name);
    void deleteByGroupName(@Param("group_name") String group_name);
}
