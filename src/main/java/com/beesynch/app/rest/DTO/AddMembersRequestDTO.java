package com.beesynch.app.rest.DTO;

public class AddMembersRequestDTO {
    private Long adminUserId;
    private String memberUsername;
    private Long memberId;

    // Constructors
    public AddMembersRequestDTO() {}

    // Getters
    public Long getAdminUserId() {
        return adminUserId;
    }
    public String getMemberUsername() {
        return memberUsername;
    }

    public Long getMemberId(){
        return memberId;
    }

    // Setters
    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }
    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }
}
