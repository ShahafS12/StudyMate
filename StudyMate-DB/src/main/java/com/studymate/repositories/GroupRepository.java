package com.studymate.repositories;

import com.studymate.model.Group;
import com.studymate.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    @Query(value = "{'groupName': ?0, 'isDeleted': false}")
    Group findByGroupName(@Param("groupName") String groupName);
    void deleteByGroupName(String groupName);
    @Query(value = "{'isDeleted': false}", fields = "{'groupName': 1, '_id': 0}")
    List<String> findAllGroupNames();
    @Query(value = "{'groupName': { $regex: ?0, $options: 'i' }, 'isDeleted': false}")
    List<Group> findByGroupNameContainingIgnoreCase(String groupName);


}
