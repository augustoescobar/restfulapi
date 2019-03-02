package com.example.restfulapi.controllers;

import com.example.restfulapi.dtos.ListPostsRequestDTO;
import com.example.restfulapi.dtos.ListPostsResponseDTO;
import com.example.restfulapi.entities.Post;
import com.example.restfulapi.services.MappingService;
import com.example.restfulapi.services.PostService;
import com.example.restfulapi.validators.PostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {
    
    private PostService postService;
    private MappingService mappingService;

    @Autowired
    public PostController(
            PostService postService,
            MappingService mappingService) {

        this.postService = postService;
        this.mappingService = mappingService;
    }

    @InitBinder("post")
    protected void postInitBinder(WebDataBinder binder) {
        binder.addValidators(new PostValidator());
    }

    @GetMapping
    public ResponseEntity<ListPostsResponseDTO> list(ListPostsRequestDTO requestDTO) {

        Pageable pageable = mappingService.toPageRequest(requestDTO);

        Page<Post> postsPage = requestDTO.getTitle() != null
                ? postService.findAllByTitleContaining(requestDTO.getTitle(), pageable)
                : postService.findAll(pageable);

        ListPostsResponseDTO responseDTO = mappingService.toListResponseDTO(postsPage, ListPostsResponseDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> get(@PathVariable(name = "postId") Integer postId) {

        Post post = postService.findById(postId);

        URI uri = ControllerLinkBuilder.linkTo(PostController.class).slash(post.getId()).toUri();

        return ResponseEntity.created(uri).body(post);
    }

    @PostMapping
    public ResponseEntity<Post> add(@RequestBody @Valid Post post) {

        postService.save(post);

        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable(name = "id", required = false) Integer id, @RequestBody @Valid Post post) {

        post.setId(id);

        postService.save(post);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer postId) {

        postService.delete(postId);

        return ResponseEntity.noContent().build();
    }
}
