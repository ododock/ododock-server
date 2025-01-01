package ododock.webserver.domain.content;

import ododock.webserver.web.v1alpha1.dto.ContentListOptions;

import java.util.List;

public interface ContentDetailsService {

    List<Content> listContents(ContentListOptions options);



}
