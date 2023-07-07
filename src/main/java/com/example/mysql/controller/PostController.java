package com.example.mysql.controller;

import com.example.mysql.application.usacase.CreatePostUsecase;
import com.example.mysql.application.usacase.GetTimelinePostsUsecase;
import com.example.mysql.domain.post.dto.DailyPostCount;
import com.example.mysql.domain.post.dto.DailyPostCountRequest;
import com.example.mysql.domain.post.dto.PostCommand;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.service.PostReadService;
import com.example.mysql.domain.post.service.PostWriteService;
import com.example.mysql.util.CursorRequest;
import com.example.mysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    final private GetTimelinePostsUsecase getTimelinePostsUsecase;
    final private CreatePostUsecase createPostUsecase;
//    @PostMapping("")
//    public Long create(@RequestBody PostCommand command) {
//        return postWriteService.create(command);
//    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCounts(@RequestBody DailyPostCountRequest request) {
        return postReadService.getDailyPostCounts(request);

        /* 요청:
        {
            "memberId": 1,
            "firstDate": "2023-07-04",
            "lastDate": "2023-07-06"
        }

        응답:
        [
          {
            "memberId": 1,
            "date": "2023-07-04",
            "postCount": 6
          }
        ]
        */
    }


    @GetMapping("/members/{memberId}")
    public Page<Post> getPosts(@PathVariable Long memberId, @RequestParam Integer page, @RequestParam Integer size) {
        return postReadService.getPosts(memberId, PageRequest.of(page, size));
        /*
        오프셋 기반의 문제점: 게시글이 많으면 전체 페이지를 구하는 카운터를 할때 db 과부화가 걸리고,
                           3페이지를 보고싶으면 1번,2번을 읽어와야 하기 때문에 불필요한 데이터 조회가 발생한다

        */
    }


    @GetMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(@PathVariable Long memberId, @RequestBody CursorRequest cursorRequest) {
        return postReadService.getPosts(memberId, cursorRequest);
        /*
        {
            "key": 0,
            "size": 0
        }
         */
    }

    /*

    검색조건이 인덱스에 부합하다면, 테이블블에 바로 접근 하는 것보다 인덱스를 통해 접근하는 것이 매우 빠르다
    테이블에 접근하지 않고 인덱스로만 데이터 응답을 내려주는것이 커버링 인덱스

     */



    /*조회 시점에 부하가 있는 Fan Out On Write (Push Model) 방식
      팔로우 수가 많아지면 큰 부하가 걸릴수 있기에 적을때 쓰는것이 유리함
      트위터에서 사용중(최대 5000명의 친구를 팔로우할수 있고, 나를 팔로우 한사람이 늘어나면 가능 (팔로워,팔로윙) )
      공간복잡도 희생
    */
    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeline(
            @PathVariable Long memberId,
           @RequestBody CursorRequest cursorRequest
    ) {
        return getTimelinePostsUsecase.execute(memberId, cursorRequest);
        /*
        {
        "size": 10
        }

        으로 보내면 팔로워 목록 10개 커서 페이징
         */
    }

    /*고민: Push Model에 적용 한다면 모든 회원의 타임라인에 배달되기 전까지 게시물 작성의 트랜잭션을 유지하는 것이 맞을까?
            유저를 팔로워 하고 있는 유저들중에 유령 회원도 있을 것인데 그 유저에게 까지 트랜잭션을 유지하는것이 맞는가?
            이것과 관련된 이론 CAP (일관성,고가용성,파티션 네트워크) 3가지의 벨런스를 모두 유지할수 없으니
            2가지만 가져가자는 이론

     */



    /*
    Write 시점에 부하가 있는 Fan Out On Write(Pull Model).
    게시물 작성시, 해당 회원을 팔로우하는 회원들에게 데이터를 배달한다.
    페이스북에서 사용중 (최대 5000명의 친구를 보유할수 있고, 내가 친구를 끊기 전까지 못늘림).
    시간복잡도 희생.
    원본 데이터를 직접 참조하므로 정합성 보장 유리하고
    팔로우 수가 많을수록 성능이 많이 떨어짐.
     */

    @PostMapping("")
    public Long create(@RequestBody PostCommand command) {
        return createPostUsecase.execute(command);
        //흐름 : 1번 유저가 게시글을 쓰면 나를 팔로우한 유저들에게 게시글 배달
    }

    @GetMapping("/members/{memberId}/timeline2")
    public PageCursor<Post> getTimeline2(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ) {
        return getTimelinePostsUsecase.executeByTimeline(memberId, cursorRequest);
    }


    @PostMapping("/posts/{postId}/like")
    public void likePost(@PathVariable Long postId) {
        postWriteService.likePost(postId);
    }

}
