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
        /*
        만약 이미지를 업로드 한다 했을때 트렌잭션이 길어질수 있어서 배제하는 것이 좋다
        길어지면 db의 커넥션을 오랫동안 점유해 동시 다발적으로 발생하게 되면 커넥션 풀 고갈로 이뤄질수있기 때문

        @@@트렌잭션 범위는 항상 짧게 가져가는것이 좋다 트렌잭션 기간 동안에는 커넥션 풀을 점유하고 있기 때문

         */

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
