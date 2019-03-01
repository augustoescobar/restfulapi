package com.example.restfulapi.controllers;

import com.example.restfulapi.dtos.ListPostsRequestDTO;
import com.example.restfulapi.dtos.ListPostsResponseDTO;
import com.example.restfulapi.entities.Post;
import com.example.restfulapi.services.MappingService;
import com.example.restfulapi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;
    private MappingService mappingService;

    @Autowired
    public PostController(PostService postService, MappingService mappingService) {
        this.postService = postService;
        this.mappingService = mappingService;
    }

    @GetMapping
    public ResponseEntity<?> list(ListPostsRequestDTO requestDTO) {

        Pageable pageable = mappingService.toPageRequest(requestDTO);

        Page<Post> postsPage = requestDTO.getTitle() != null
                ? postService.findAllByTitle(requestDTO.getTitle(), pageable)
                : postService.findAll(pageable);

        ListPostsResponseDTO responseDTO = mappingService.toListResponseDTO(postsPage, ListPostsResponseDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> get(@PathVariable(name = "postId") Integer postId) {

        Post post = postService.findById(postId);

        URI uri = ControllerLinkBuilder.linkTo(PostController.class).slash(post.getId()).toUri();

        return ResponseEntity.created(uri).body(post);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Post post) {

        postService.save(post);

        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, @RequestBody Post post) {

        post.setId(id);

        postService.save(post);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Integer postId) {

        postService.delete(postId);

        return ResponseEntity.noContent().build();
    }
}
