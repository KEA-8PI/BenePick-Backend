package com._pi.benepick.domain.members.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class MembersRequest {


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class MembersRequestDTO{

        private String deptName;
        private String name;
        private Long point;
        private Long penaltyCnt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class MemberPointFileDTO{
        private MultipartFile file;

    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class MemberPasswordDTO{
        private String password;
    }
}
