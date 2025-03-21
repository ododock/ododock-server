package ododock.webserver.domain.content;

import java.util.List;

public interface ContentDetailsService {

    List<MediaContent> listContents(ContentListOptions options);

}
