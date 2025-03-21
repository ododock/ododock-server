package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class V1alpha1Styles {

    Boolean bold;
    Boolean italic;
    Boolean underline;
    Boolean strikethrough;
    String textColor;
    String backgroundColor;

}
