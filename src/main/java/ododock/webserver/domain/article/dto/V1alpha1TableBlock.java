package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class V1alpha1TableBlock extends V1alpha1BaseBlock {

    String type = "table";
    List<V1alpha1InlineContent> content;
    List<V1alpha1BaseBlock> children;

    public V1alpha1TableBlock() {
        this.children = new ArrayList<>();
    }

}
