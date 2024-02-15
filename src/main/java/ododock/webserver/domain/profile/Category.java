package ododock.webserver.domain.profile;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
public class Category {

    @Column(name = "profile_id", insertable = false, updatable = false)
    private Long profileId;

    @Column(name = "category_name", nullable = false)
    private String name;

}
