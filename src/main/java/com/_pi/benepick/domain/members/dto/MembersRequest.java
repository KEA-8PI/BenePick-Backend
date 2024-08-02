package com._pi.benepick.domain.members.dto;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
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
        private Role role;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminMemberRequestDTO{
        private String id;
        private String deptName;
        private String name;
        private Long point;
        private Long penaltyCnt;
        private Role role;

        public Members toEntity(AdminMemberRequestDTO adminMemberRequestDTO){
            return Members.builder()
                    .id(adminMemberRequestDTO.getId())
                    .role(adminMemberRequestDTO.getRole())
                    .penaltyCnt(adminMemberRequestDTO.getPenaltyCnt())
                    .point(adminMemberRequestDTO.getPoint())
                    .name(adminMemberRequestDTO.getName())
                    .deptName(adminMemberRequestDTO.getDeptName())
                    .build();
        }
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
