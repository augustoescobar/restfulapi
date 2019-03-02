package com.example.restfulapi.services;

import com.example.restfulapi.entities.Post;
import com.example.restfulapi.repositiories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {

        this.postRepository = postRepository;
    }

    public Page<Post> findAll(Pageable pageable) {

        return postRepository.findAll(pageable);
    }

    public Page<Post> findAllByTitleContaining(String title, Pageable pageable) {

        return postRepository.findAllByTitleContaining(title, pageable);
    }

    public Post findById(Integer id) {

        return postRepository.findById(id).orElse(null);
    }

    public void save(Post post) {

        postRepository.save(post);
    }

    public void delete(Integer id) {

        postRepository.deleteById(id);
    }

}
