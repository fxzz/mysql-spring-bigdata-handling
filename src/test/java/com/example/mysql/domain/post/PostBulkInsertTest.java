package com.example.mysql.domain.post;

import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.repository.PostRepository;
import com.example.mysql.util.PostFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {
   @Autowired
   private PostRepository postRepository;

    @Test
    public void bulkInsert() {
        var easyRandom = PostFixtureFactory.get(
                3L,
                LocalDate.of(2021,6,4),
                LocalDate.of(2023,7,5)
                );


        var queryStopWatch = new StopWatch();


     var posts= IntStream.range(0,10000 * 100)
             .parallel()
             .mapToObj(value -> easyRandom.nextObject(Post.class))
             .toList();


        queryStopWatch.start();
     postRepository.bulkInsert(posts);
        queryStopWatch.stop();
        System.out.println("쿼리시간"+ queryStopWatch.getTotalTimeSeconds());
    }

    /*

    테스트 환경
    select memberId, count(id) from post group by memberId;
    memberId   count(id)
        1        2000000
        2        1000000
        3        4000000

    select count(distinct (createdDate)) from post;
        761


    인덱스가 없을때
    explain SELECT memberId, createdDate, count(id) as cnt
    FROM post
    WHERE memberId = 3 and createdDate between '1900-01-01' and '2023-12-25'
    GROUP BY memberId, createdDate;

    type   rows                 실행시간  3초
    ALL    6806454


    인덱스가 있을때
    explain SELECT memberId, createdDate, count(id) as cnt
    FROM post use index (POST__index_member_id)
    WHERE memberId = 3 and createdDate between '1900-01-01' and '2023-12-25'
    GROUP BY memberId, createdDate;

     type   rows     filtered      실행시간  15초
     ref    3403227  11.11



     explain SELECT memberId, createdDate, count(id) as cnt
     FROM post use index (POST__index_created_date)
     WHERE memberId = 3 and createdDate between '1900-01-01' and '2023-12-25'
     GROUP BY memberId, createdDate;

     type     rows        filtered     실행시간  1분이상
     index    6806454     5


     explain SELECT memberId, createdDate, count(id) as cnt
     FROM post use index (POST__index_member_id_created_date)
     WHERE memberId = 3 and createdDate between '1900-01-01' and '2023-12-25'
     GROUP BY memberId, createdDate;

     type     rows         filtered     실행시간  0.3초
     range    3403227      100


    인덱스는 데이터 분포도에 큰 영향을 미친다.
    */


}
