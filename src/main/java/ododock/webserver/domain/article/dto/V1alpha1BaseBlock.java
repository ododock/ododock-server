package ododock.webserver.domain.article.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class V1alpha1BaseBlock {

    private String id;
    private String type;
    private V1alpha1DefaultProps props;
    private List<V1alpha1InlineContent> content;
    private List<V1alpha1BaseBlock> children;

    public V1alpha1BaseBlock() {
        children = new ArrayList<>();
    }

}
