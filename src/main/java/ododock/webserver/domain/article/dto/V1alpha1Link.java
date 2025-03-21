package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class V1alpha1Link extends V1alpha1InlineContent {

    String type = "link";
    List<V1alpha1StyledText> content;
    String href;

    public V1alpha1Link() {
        this.content = new ArrayList<>();
    }

}
