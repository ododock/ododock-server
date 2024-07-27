package ododock.webserver.client;

import ododock.webserver.domain.content.Content;
import org.springframework.lang.Nullable;

public interface ContentQueryService {

    public Content search(@Nullable String keyword);

}
