package com.example.mysql.application.usacase;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.service.FollowReadService;
import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetFollowingMembersUsecase {
    final private MemberReadService memberReadService;
    final private FollowReadService followReadService;

    public List<MemberDto> execute(Long memberId) {
        var followings = followReadService.getFollowings(memberId);
        var followingMemberIds = followings.stream().map(Follow::getToMemberId).toList();
        return memberReadService.getMembers(followingMemberIds);
    }
}