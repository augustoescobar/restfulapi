package com.example.restfulapi.init;

import com.example.restfulapi.entities.Authority;
import com.example.restfulapi.entities.Post;
import com.example.restfulapi.entities.User;
import com.example.restfulapi.enums.Authorities;
import com.example.restfulapi.services.AuthorityService;
import com.example.restfulapi.services.PostService;
import com.example.restfulapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Bootstrap {

    private PostService postService;
    private AuthorityService authorityService;
    private UserService userService;

    @Autowired
    public Bootstrap(
            PostService postService,
            AuthorityService authorityService,
            UserService userService) {

        this.postService = postService;
        this.authorityService = authorityService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {

        loadPosts();

        loadAuthorities();

        loadUsers();
    }

    @PreDestroy
    public void destroy() { }

    private void loadPosts() {

        if (postService.count() == 0) {

            for (int i = 0; i < 10; i++) {

                Post post = new Post();
                post.setTitle("Example " + i);
                post.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

                postService.save(post);
            }
        }
    }

    private void loadAuthorities() {

        if (authorityService.count() == 0) {

            Authority authority = new Authority();
            authority.setAuthority(Authorities.ADMIN.name());

            authorityService.save(authority);
        }
    }

    private void loadUsers() {

        if (userService.count() == 0) {

            User user = new User();
            user.setEnabled(true);
            user.setUsername("admin");
            user.setPassword(userService.encodePassword("1234"));
            user.setAuthorities(authorityService.findAll());

            userService.save(user);

            user = new User();
            user.setEnabled(true);
            user.setUsername("admin2");
            user.setPassword(userService.encodePassword("1234"));
            user.setAuthorities(authorityService.findAll());

            userService.save(user);
        }
    }
}
