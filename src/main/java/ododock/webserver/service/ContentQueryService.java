package ododock.webserver.service;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.content.ContentApiClient;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContentQueryService {

    private final ContentApiClient contentApiClient;



}
