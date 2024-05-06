package com.syntaxerror.seminario.repository;

import com.syntaxerror.seminario.model.UsuarioMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioMongoRepository extends MongoRepository<UsuarioMongo, String> {
    UsuarioMongo findByEmail(String email);
}