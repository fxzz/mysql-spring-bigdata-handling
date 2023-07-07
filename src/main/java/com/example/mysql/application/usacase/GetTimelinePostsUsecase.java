package com.example.mysql.application.usacase;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.service.FollowReadService;
import com.example.mysql.domain.post.dto.PostDto;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.entity.Timeline;
import com.example.mysql.domain.post.service.PostReadService;
import com.example.mysql.domain.post.service.TimelineReadService;
import com.example.mysql.util.CursorRequest;
import com.example.mysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetTimelinePostsUsecase {
    final private FollowReadService followReadService;
    final private TimelineReadService timelineReadService;
    final private PostReadService postReadService;

    public PageCursor<PostDto> execute(Long memberId, CursorRequest cursorRequest) {
        /*
        1. memberId -> follow 조회
        2. 1번 결과로 게시물 조회
         */
        var follows = followReadService.getFollowings(memberId);
        var followerMemberIds = follows
                .stream()
                .map(Follow::getToMemberId)
                .toList();

        return postReadService.getPostDtos(followerMemberIds, cursorRequest);
    }

    public PageCursor<PostDto> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
        /*
            1. Timeline 조회
            2. 1번에 해당하는 게시물을 조회한다.
        */
        var pagedTimelines = timelineReadService.getTimelines(memberId, cursorRequest);
        var postIds = pagedTimelines.body().stream().map(Timeline::getPostId).toList();
        var posts = postReadService.getPostDtos(postIds);
        return new PageCursor<>(pagedTimelines.nextCursorRequest(), posts);
    }
}
