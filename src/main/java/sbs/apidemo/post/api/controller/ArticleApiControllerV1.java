package sbs.apidemo.post.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sbs.apidemo.post.api.dto.ArticleDto;
import sbs.apidemo.post.api.vo.ListArticle;
import sbs.apidemo.base.argumentresolver.dto.Dto;

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
