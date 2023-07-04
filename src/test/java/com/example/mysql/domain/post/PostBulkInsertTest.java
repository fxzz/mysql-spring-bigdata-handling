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
                LocalDate.of(2023,6,4),
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



}
