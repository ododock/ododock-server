package ododock.webserver.domain;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import ododock.webserver.domain.article.ArticleExcerptor;
import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockNodeExcerptTest {

    @Test
    public void testExcerpt() {
        String jsonString = """
                [
                    {
                        "id": "9ab769ea-cfdb-414d-a190-5e9d1dc29d85",
                        "type": "heading",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left",
                            "level": 1
                        },
                        "content": [
                            {
                            "type": "text",
                            "text": "This is sample article heading",
                            "styles": {}
                            }
                        ],
                        "children": []
                        },
                        {
                        "id": "3f18f50d-fa94-4d37-b8ad-1661d7249f33",
                        "type": "heading",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left",
                            "level": 2
                        },
                        "content": [
                            {
                            "type": "text",
                            "text": "Table of contents",
                            "styles": {}
                            }
                        ],
                        "children": []
                        },
                        {
                        "id": "3b4f8d56-cf4e-4c59-b14e-1a90949154a0",
                        "type": "numberedListItem",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left"
                        },
                        "content": [
                            {
                            "type": "text",
                            "text": "abstract",
                            "styles": {}
                            }
                        ],
                        "children": []
                        },
                        {
                        "id": "09b8e157-14c8-4d74-a408-7363cdbc467d",
                        "type": "numberedListItem",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left"
                        },
                        "content": [
                            {
                            "type": "text",
                            "text": "body",
                            "styles": {}
                            }
                        ],
                        "children": []
                        },
                        {
                        "id": "6e1c46b8-11ba-4aa1-bb33-2d12e9b65cd2",
                        "type": "numberedListItem",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left"
                        },
                        "content": [
                            {
                            "type": "text",
                            "text": "summary",
                            "styles": {}
                            }
                        ],
                        "children": []
                        },
                        {
                        "id": "2a870bb9-4220-40da-965e-1c37acd8d2a1",
                        "type": "paragraph",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left"
                        },
                        "content": [],
                        "children": []
                        },
                        {
                        "id": "bc5e0274-3942-4541-8ccb-adaa81e1011f",
                        "type": "heading",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left",
                            "level": 3
                        },
                        "content": [
                            {
                            "type": "text",
                            "text": "abstract",
                            "styles": {}
                            }
                        ],
                        "children": []
                        },
                        {
                        "id": "944c73d9-e4d6-4f8d-a178-f8cebcf52ec2",
                        "type": "paragraph",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left"
                        },
                        "content": [
                            {
                            "type": "text",
                            "text": "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus fringilla lacus nec metus bibendum egestas iaculis massa nisl malesuada lacinia integer nunc posuere ut hendrerit semper vel class aptent taciti sociosqu ad litora torquent per conubia nostra inceptos himenaeos orci varius natoque penatibus et magnis.",
                            "styles": {}
                            }
                        ],
                        "children": []
                        },
                        {
                        "id": "d49597c1-8028-4aac-9b60-5b4f8c46d62b",
                        "type": "paragraph",
                        "props": {
                            "textColor": "default",
                            "backgroundColor": "default",
                            "textAlignment": "left"
                        },
                        "content": [],
                        "children": []
                        }
                    ]
                """;

        Type listType = new TypeToken<List<V1alpha1BaseBlock>>() {
        }.getType();
        List<V1alpha1BaseBlock> blocks = new Gson().fromJson(jsonString, listType);
        String excerpt = ArticleExcerptor.from(blocks, 200);

        assertThat(excerpt).isNotBlank();
        assertThat(excerpt).startsWith("This is sample article heading Table of contents");
    }


}
