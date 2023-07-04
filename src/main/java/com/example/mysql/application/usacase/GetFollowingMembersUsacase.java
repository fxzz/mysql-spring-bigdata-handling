package com.example.mysql.application.usacase;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.service.FollowReadService;
import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetFollowingMembersUsacase {
    final private MemberReadService memberReadService;
    final private FollowReadService followReadService;

    public List<Member> execute(Long memberId) {
        var follows = followReadService.getFollowings(memberId);
        var memberIds = follows
                .stream()
                .map(Follow::getToMemberId)
                .collect(Collectors.toList());
        return memberReadService.getMembers(memberIds);
    }
}