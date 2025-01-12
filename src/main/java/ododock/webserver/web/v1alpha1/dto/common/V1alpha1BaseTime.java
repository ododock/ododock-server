package ododock.webserver.web.v1alpha1.dto.common;

import lombok.Data;

@Data
public class V1alpha1BaseTime {

    private Long createdAt;
    private Long updatedAt;

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Long updatedAt) {
        this.updatedAt = updatedAt;
    }

}
