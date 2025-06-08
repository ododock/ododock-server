package ododock.webserver.domain.content;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ododock.webserver.domain.ListOptions;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ContentListOptions extends ListOptions {
}
