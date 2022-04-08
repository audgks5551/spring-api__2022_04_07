package sbs.apidemo.post.api.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ListArticle {
    @NotNull(message = "title cannot be null")
    private String title;
    @NotNull(message = "content cannot be null")
    private String content;
}
