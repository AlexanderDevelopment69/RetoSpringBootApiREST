package com.bancocomercio.repository;

import com.bancocomercio.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUsuarioId(Long usuarioId);

}