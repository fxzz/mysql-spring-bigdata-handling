package com.example.mysql.controller;

import com.example.mysql.application.usacase.CreateFollowMemberUsecase;
import com.example.mysql.application.usacase.GetFollowingMembersUsecase;
import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.entity.Member;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
    final private CreateFollowMemberUsecase createFollowMemberUsecase;
    final private GetFollowingMembersUsecase getFollowingMembersUsacase;


    @PostMapping("/{fromId}/{toId}")
    public List<MemberDto> register(@PathVariable Long fromId, @PathVariable Long toId) {
        createFollowMemberUsecase.execute(fromId, toId);
        return getFollowingMembersUsacase.execute(fromId);
    }


    @GetMapping("/members/{fromId}")
    public List<MemberDto> getFollowers(@PathVariable Long fromId) {
        return getFollowingMembersUsacase.execute(fromId);
    }
}