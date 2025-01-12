package ododock.webserver.web.v1alpha1.dto;

import lombok.Data;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Account;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Profile;
import ododock.webserver.web.v1alpha1.dto.common.V1alpha1Base;

import java.util.Set;

@Data
public class V1alpha1Article extends V1alpha1Base {

    private String id;
    private String title;
    private String body;
    private Boolean visibility;
    private V1alpha1Account ownerAccount;
    private V1alpha1Profile ownerProfile;
    private String createdAt;
    private String updatedAt;
    private String category;
    private Set<String> tags;

}
