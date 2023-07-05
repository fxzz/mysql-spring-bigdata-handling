package com.example.mysql.controller;

import com.example.mysql.domain.post.dto.DailyPostCount;
import com.example.mysql.domain.post.dto.DailyPostCountRequest;
import com.example.mysql.domain.post.dto.PostCommand;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.service.PostReadService;
import com.example.mysql.domain.post.service.PostWriteService;
import com.example.mysql.util.CursorRequest;
import com.example.mysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;

    @PostMapping("")
    public Long create(@RequestBody PostCommand command) {
        return postWriteService.create(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCounts(@RequestBody DailyPostCountRequest request) {
        return postReadService.getDailyPostCount(request);

        /* 요청:
        {
            "memberId": 1,
            "firstDate": "2023-07-04",
            "lastDate": "2023-07-06"
        }

        응답:
        [
          {
            "memberId": 1,
            "date": "2023-07-04",
            "postCount": 6
          }
        ]
        */
    }


    @GetMapping("/members/{memberId}")
    public Page<Post> getPosts(@PathVariable Long memberId, @RequestParam Integer page, @RequestParam Integer size) {
        return postReadService.getPosts(memberId, PageRequest.of(page, size));
        /*
        오프셋 기반의 문제점: 게시글이 많으면 전체 페이지를 구하는 카운터를 할때 db 과부화가 걸리고,
                           3페이지를 보고싶으면 1번,2번을 읽어와야 하기 때문에 불필요한 데이터 조회가 발생한다

        */
    }


    @GetMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(@PathVariable Long memberId, @RequestBody CursorRequest cursorRequest) {
        return postReadService.getPosts(memberId, cursorRequest);
        /*
        {
            "key": 0,
            "size": 0
        }
         */
    }

    /*

    검색조건이 인덱스에 부합하다면, 테이블블에 바로 접근 하는 것보다 인덱스를 통해 접근하는 것이 매우 빠르다
    테이블에 접근하지 않고 인덱스로만 데이터 응답을 내려주는것이 커버링 인덱스



     */


}
