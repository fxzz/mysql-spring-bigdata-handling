package com.example.mysql.controller;

import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.member.dto.RegisterMemberCommand;
import com.example.mysql.domain.member.entity.Member;
import com.example.mysql.domain.member.service.MemberReadService;
import com.example.mysql.domain.member.service.MemberWriteService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @PostMapping("/members")
    public Member register(@RequestBody RegisterMemberCommand command) {
       return memberWriteService.register(command);
    }

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable Long id) {
       return memberReadService.getMember(id);
    }
}
