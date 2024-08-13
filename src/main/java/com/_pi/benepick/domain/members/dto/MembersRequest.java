package com._pi.benepick.domain.members.dto;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.validator.EnumValid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class MembersRequest {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class MembersRequestDTO{

        @NotBlank(message = "deptName is mandatory")
        private String deptName;

        @NotBlank(message = "name is mandatory")
        private String name;

        @NotBlank(message = "point is mandatory")
        private Long point;

        @NotBlank(message = "penaltyCnt is mandatory")
        private Long penaltyCnt;

        @NotBlank(message = "role is mandatory")
        @EnumValid(enumClass = Role.class, message = "Invalid role")
        private Role role;

    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class DeleteMembersRequestDTO{
        private List<String> id;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminMemberRequestDTO{
        @NotBlank(message = "id is mandatory")
        private String id;
        @NotBlank(message = "deptName is mandatory")
        private String deptName;
        @NotBlank(message = "name is mandatory")
        private String name;
        @NotBlank(message = "point is mandatory")
        private Long point;
        @NotBlank(message = "penaltyCnt is mandatory")
        private Long penaltyCnt;
        @NotBlank(message = "role is mandatory")
        @EnumValid(enumClass = Role.class, message = "Invalid role")
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
        @NotBlank(message = "password is mandatory")
        private String password;
    }
}
