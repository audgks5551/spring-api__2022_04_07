package sbs.apidemo.api.article.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sbs.apidemo.api.article.vo.ListArticle;
import sbs.apidemo.argumentresolver.Dto;


import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/api/v1/article")
public class ArticleApiControllerV1 implements ArticleApiController {

    @Override
    @GetMapping
    public String Home(@Valid @Dto ListArticle listArticle) {
        log.info("{}", listArticle);
        return "hello";
    }
}
