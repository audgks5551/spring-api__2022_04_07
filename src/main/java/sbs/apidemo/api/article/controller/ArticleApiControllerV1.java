package sbs.apidemo.api.article.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sbs.apidemo.api.article.dto.ArticleDto;
import sbs.apidemo.api.article.vo.ListArticle;
import sbs.apidemo.argumentresolver.Dto;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api/v1/article")
public class ArticleApiControllerV1 {

    @GetMapping
    public ResponseEntity<?> Home(
            @Valid @Dto(vo = ListArticle.class) ArticleDto articleDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            /**
             * error code와 message 확인
             */
            for (Object object : bindingResult.getAllErrors()) {
                if(object instanceof ObjectError) {
                    ObjectError objectError = (ObjectError) object;
                    System.out.println(objectError.getCode());
                    System.out.println(objectError.getDefaultMessage());
                }
            }

            return ResponseEntity.status(BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        return ResponseEntity.status(OK).body("ok");
    }
}
