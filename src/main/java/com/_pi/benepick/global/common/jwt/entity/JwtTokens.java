package com._pi.benepick.global.common.jwt.entity;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash("JwtTokens")
public class JwtTokens {

    @Id
    private String id;      // access token을 id로 사용
    @Getter
    private String refreshToken;
    @TimeToLive
    private long ttl;

    public String getAccessToken() {
        return id;
    }
}