package com.example.mysql.util;

import com.example.mysql.domain.post.entity.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;

public class PostFixtureFactory {
    static public EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lasDate) {
        var idField = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));


        var memberIdField = named("memberId")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        var param = new EasyRandomParameters()
                .excludeField(idField)
                .dateRange(firstDate, lasDate)
                .randomize(memberIdField, () -> memberId);
        return new EasyRandom(param);
    }
}
