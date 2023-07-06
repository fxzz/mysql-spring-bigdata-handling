package com.example.mysql.application.usacase;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.service.FollowReadService;
import com.example.mysql.domain.post.dto.PostCommand;
import com.example.mysql.domain.post.service.PostWriteService;
import com.example.mysql.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {
    final private PostWriteService postWriteService;
    final private FollowReadService followReadService;
    final private TimelineWriteService timelineWriteService;

    @Transactional
    public Long execute(PostCommand command) {
        var postId = postWriteService.create(command);

        var followerMemberIds = followReadService
                .getFollowers(command.memberId()).stream()
                .map((Follow::getFromMemberId))
                .toList();

        timelineWriteService.deliveryToTimeLine(postId, followerMemberIds);

        return postId;
        /*
        나를 팔로우 하고 있는 사람이 100만 명이면 100만건의 insert가 추가로 생김
        그렇기에 성능이 좋은 캐시db를 써도 되고 비동기로 해도 됨
        */
    }
}
