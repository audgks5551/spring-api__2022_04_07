package sbs.apidemo.api.article.controller;

import sbs.apidemo.api.article.dto.ArticleDto;
import sbs.apidemo.api.article.vo.ListArticle;

public interface ArticleApiController {
    String Home(ListArticle listArticle);
}
