package com.vvw.licensingservice.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("organization")
public record OrganizationDto(
        @Id
        String id,
        String name,
        String contactName,
        String contactEmail,
        String contactPhone) {
}