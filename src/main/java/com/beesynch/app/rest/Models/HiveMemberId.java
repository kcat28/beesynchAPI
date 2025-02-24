package com.beesynch.app.rest.Models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class HiveMemberId implements Serializable {

    private Long userId;
    private Long hiveId;

    // Default constructor, getters, and setters
    public HiveMemberId() {}

    public HiveMemberId(Long userId, Long hiveId) {
        this.userId = userId;
        this.hiveId = hiveId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHiveId() {
        return hiveId;
    }

    public void setHiveId(Long hiveId) {
        this.hiveId = hiveId;
    }

    // Override equals and hashCode methods for correct comparison in collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HiveMemberId that = (HiveMemberId) o;

        if (!userId.equals(that.userId)) return false;
        return hiveId.equals(that.hiveId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + hiveId.hashCode();
        return result;
    }
}
