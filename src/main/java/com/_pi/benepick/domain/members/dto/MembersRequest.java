package com._pi.benepick.domain.members.dto;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.validator.EnumValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class MembersRequest {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class MembersRequestDTO{

        @NotBlank(message = "부서는 필수값입니다.")
        private String deptName;

        @NotBlank(message = "사원이름은 필수값입니다.")
        private String name;

        @NotNull(message = "포인트는 필수값입니다.")
        private Long point;

        @NotNull(message = "패널티수는 필수값입니다.")
        private Long penaltyCnt;

        @NotNull(message = "역할은 필수값입니다.")
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
        @NotBlank(message = "아이디는 필수값입니다.")
        private String id;
        @NotBlank(message = "부서는 필수값입니다.")
        private String deptName;
        @NotBlank(message = "사원이름은 필수값입니다.")
        private String name;
        @NotNull(message = "포인트는 필수값입니다.")
        @Min(value = 0,message = "포인트는 0 이상 입력가능합니다.")
        private Long point;
        @NotNull(message = "패널티수는 필수값입니다.")
        private Long penaltyCnt;
        @NotNull(message = "역할은 필수값입니다.")
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
        @NotBlank(message = "비밀번호는 필수값입니다.")
        private String password;
    }
}
