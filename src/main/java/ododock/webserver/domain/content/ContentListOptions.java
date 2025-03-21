package ododock.webserver.domain.content;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ododock.webserver.domain.ListOptions;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ContentListOptions extends ListOptions {
}
