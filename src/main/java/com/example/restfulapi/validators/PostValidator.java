package com.example.restfulapi.validators;

import com.example.restfulapi.entities.Post;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("postValidator")
public class PostValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Post.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Post post = (Post) target;

        if ("api".equals(post.getTitle())) {

            errors.rejectValue("title", null,"title cannot be 'api'");
        }
    }
}