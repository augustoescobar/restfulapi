package com.example.restfulapi.controllers;

import com.example.restfulapi.dtos.ListPostsRequestDTO;
import com.example.restfulapi.dtos.ListPostsResponseDTO;
import com.example.restfulapi.entities.Post;
import com.example.restfulapi.services.MappingService;
import com.example.restfulapi.services.PostService;
import com.example.restfulapi.validators.PostValidator;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Api(value = "Post", description = "REST API for Post", tags = { "Post" })
public class PostController {

    private Logger logger = LoggerFactory.getLogger(PostController.class);

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

        logger.info("[PostController::list] listing posts by query string " + mappingService.toJson(requestDTO) + ": " + mappingService.toJson(responseDTO));

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable(name = "id") Long id) {

        Post post = postService.findById(id);

        if (post == null) {

            logger.info("[PostController::get] no post found with id " + id);

            return ResponseEntity.notFound().build();
        }

        logger.info("[PostController::get] post found with id " + id + ": " + mappingService.toJson(post));

        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> add(@RequestBody @Valid Post post) {

        try {

            postService.save(post);
        } catch (Exception e) {

            logger.error("[PostController::add] Failed to save new post in database: " + mappingService.toJson(post), e);

            throw e;
        }

        logger.info("[PostController::add] saving new post saved into database: " + mappingService.toJson(post));

        URI uri = ControllerLinkBuilder.linkTo(PostController.class).slash(post.getId()).toUri();

        return ResponseEntity.created(uri).body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable(name = "id", required = false) Long id, @RequestBody @Valid Post post) {

        Post oldPost = postService.findById(id);

        if (oldPost == null) {

            logger.warn("[PostController::update] no post found with id " + id);

            return ResponseEntity.notFound().build();
        }

        post.setId(id);

        try {

            postService.save(post);
        } catch (Exception e) {

            logger.error("[PostController::update] Failed to update post in database: " + mappingService.toJson(post), e);

            throw e;
        }

        logger.info("[PostController::update] post information updated: { \"old\": " + mappingService.toJson(oldPost) + ", \"new\": " + mappingService.toJson(post) + " }");

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {

        Post post = postService.findById(id);

        if (post == null) {

            logger.warn("[PostController::delete] no post found with id " + id);

            return ResponseEntity.notFound().build();
        }

        try {

            postService.delete(post);
        } catch (Exception e) {

            logger.error("[PostController::delete] Failed to delete post: " + mappingService.toJson(post), e);
        }

        logger.info("[PostController::delete] post with id " + id + " deleted successfully");

        return ResponseEntity.noContent().build();
    }
}
