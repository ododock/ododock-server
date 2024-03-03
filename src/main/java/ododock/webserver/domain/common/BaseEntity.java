package ododock.webserver.domain.common;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class BaseEntity extends BaseTimeEntity {
}
