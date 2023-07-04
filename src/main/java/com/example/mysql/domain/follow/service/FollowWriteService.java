package com.example.mysql.domain.follow.service;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.repository.FollowRepository;
import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
public class FollowWriteService {
    final private FollowRepository followRepository;

    public Follow create(Member fromMember, Member toMember) {
        if (fromMember.getId().equals(toMember.getId())) {
            throw new IllegalArgumentException("From, To 회원이 동일합니다");
        }

        var follow = Follow
                .builder()
                .fromMemberId(fromMember.getId())
                .toMemberId(toMember.getId())
                .build();

        return followRepository.save(follow);
    }
}