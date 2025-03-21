package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class V1alpha1BulletListItemBlock extends V1alpha1BaseBlock {

    String type = "bulletListItem";

}
