package com.example.mysql.util;


import com.example.mysql.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFixtureFactory {

    public static Member create() {

        var param = new EasyRandomParameters();
        return new EasyRandom(param).nextObject(Member.class);
    }

   public static Member create(Long seed) {

       var param = new EasyRandomParameters().seed(seed);
       return new EasyRandom(param).nextObject(Member.class);
   }
}
