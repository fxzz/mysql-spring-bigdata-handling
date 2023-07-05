package com.example.mysql.application.usacase;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.service.FollowReadService;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.service.PostReadService;
import com.example.mysql.util.CursorRequest;
import com.example.mysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetTimelinePostsUsecase {
    final private FollowReadService followReadService;

    final private PostReadService postReadService;

    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
        /*
        1. memberId -> follow 조회
        2. 1번 결과로 게시물 조회
         */
        var follows = followReadService.getFollowings(memberId);
        var followerMemberIds = follows
                .stream()
                .map(Follow::getToMemberId)
                .toList();

        return postReadService.getPosts(followerMemberIds, cursorRequest);
    }
}
