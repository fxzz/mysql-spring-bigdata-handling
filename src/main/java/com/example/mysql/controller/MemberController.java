package com.example.mysql.controller;

import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.mysql.domain.member.dto.RegisterMemberCommand;
import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.service.MemberReadService;
import com.example.mysql.domain.member.service.MemberWriteService;
import io.swagger.annotations.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    final private MemberWriteService memberWriteService;
    final private MemberReadService memberReadService;



    @PostMapping("")
    public MemberDto register(@RequestBody RegisterMemberCommand command) {
        var member = memberWriteService.create(command);
        return memberReadService.toDto(member);
    }


    @GetMapping("/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }


    @PostMapping("/{id}/name")
    public MemberDto changeNickname(
            @PathVariable Long id,
            @RequestBody String name
    ) {
        memberWriteService.changeNickname(id, name);
        return memberReadService.getMember(id);
    }


    @GetMapping("/{id}/name-histories")
    public List<MemberNicknameHistoryDto> getMemberNameHistories(@PathVariable Long id) {
        return memberReadService.getNicknameHistories(id);
    }
}