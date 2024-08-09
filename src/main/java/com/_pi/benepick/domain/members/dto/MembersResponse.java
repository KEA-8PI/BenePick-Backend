package com._pi.benepick.domain.members.dto;

import com._pi.benepick.domain.members.entity.Members;

import lombok.*;


import java.util.List;

public class MembersResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MembersDetailResponseDTO{
        private String id;
        private String name;
        private String deptName;
        private Long point;
        private Long penaltyCnt;

        public static MembersDetailResponseDTO from(Members members){
            return MembersDetailResponseDTO.builder()
                    .id(members.getId())
                    .name(members.getName())
                    .deptName(members.getDeptName())
                    .penaltyCnt(members.getPenaltyCnt())
                    .point(members.getPoint())
                    .build();
        }
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class MembersDetailListResponseDTO{
        private List<MembersDetailResponseDTO> membersDetailResponseDTOList;
        private int totalCnt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Setter
    public static class MemberPointDTO{
        private String id;
        private Long point;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Setter
    public static class MembersuccessDTO{
        private String msg;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Setter
    public static class DeleteResponseDTO{
        private List<String> memberid;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class MemberIDResponseDTO{
        private String id;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class MemberIDListResponseDTO{
        private List<String> id;
    }

}
