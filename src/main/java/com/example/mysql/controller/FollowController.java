package com.example.mysql.controller;

import com.example.mysql.application.usacase.FollowMemberUsecase;
import com.example.mysql.application.usacase.GetFollowingMembersUsacase;
import com.example.mysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowMemberUsecase followMemberUsecase;

    private final GetFollowingMembersUsacase getFollowingMembersUsacase;

    @PostMapping("/{fromId}/{toId}")
    public void register(@PathVariable Long fromId ,@PathVariable Long toId) {
        followMemberUsecase.execute(fromId, toId);
    }

    @GetMapping("/members/{fromId}")
    public List<Member> get(@PathVariable Long fromId) {
        return getFollowingMembersUsacase.execute(fromId);
    }
}
