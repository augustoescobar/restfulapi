package com.example.restfulapi.dtos;

public class ListPostsRequestDTO extends AbstractListRequestDTO {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
