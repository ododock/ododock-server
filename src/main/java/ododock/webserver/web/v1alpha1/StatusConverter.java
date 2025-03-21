package ododock.webserver.web.v1alpha1;

import ododock.webserver.web.exceptionhandler.response.Status;
import ododock.webserver.web.v1alpha1.dto.V1alpha1Status;

public class StatusConverter {

    public V1alpha1Status convert(Status status) {
        V1alpha1Status dto = new V1alpha1Status();
        dto.setType(status.type());
        dto.setDetail(status.detail());

        return dto;
    }

}
