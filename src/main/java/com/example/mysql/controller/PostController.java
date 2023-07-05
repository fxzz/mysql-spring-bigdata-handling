package com.example.mysql.controller;

import com.example.mysql.domain.post.dto.DailyPostCount;
import com.example.mysql.domain.post.dto.DailyPostCountRequest;
import com.example.mysql.domain.post.dto.PostCommand;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.service.PostReadService;
import com.example.mysql.domain.post.service.PostWriteService;
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
    }
}
