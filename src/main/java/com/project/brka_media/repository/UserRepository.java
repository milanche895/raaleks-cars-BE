package com.project.brka_media.repository;

import com.project.brka_media.entity.UserEntity;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    List<UserEntity> findAll();
    UserEntity findOneById(String id);
    UserEntity findByUsername(String username);
}
