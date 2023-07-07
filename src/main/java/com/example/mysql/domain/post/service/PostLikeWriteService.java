package com.example.mysql.domain.post.service;

import com.example.mysql.domain.member.dto.MemberDto;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.entity.PostLike;
import com.example.mysql.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostLikeWriteService {
    final private PostLikeRepository postLikeRepository;

    public void create(Post post, MemberDto memberDto) {
        var postLike = PostLike.builder()
                .postId(post.getId())
                .memberId(memberDto.id())
                .build();
        postLikeRepository.save(postLike);
    }
}