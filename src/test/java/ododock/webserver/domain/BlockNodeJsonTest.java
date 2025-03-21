package ododock.webserver.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import ododock.webserver.domain.article.dto.V1alpha1DefaultProps;
import ododock.webserver.domain.article.dto.V1alpha1InlineContent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockNodeJsonTest {

    @Test
    void paragraph_block() throws Exception {
        // given
        String jsonString = """
                {
                  "id": "123449cf-7c83-49bf-aa3b-624b03d454a9",
                  "type": "paragraph",
                  "props": {
                    "textColor": "default",
                    "backgroundColor": "default",
                    "textAlignment": "left"
                  },
                  "content": [{
                    "type": "text",
                    "text": "Welcome to this demo!",
                    "styles": {}
                  }],
                  "children": []
                }
                """;

        ObjectMapper mapper = new ObjectMapper();

        // when
        V1alpha1BaseBlock block = new Gson().fromJson(jsonString, V1alpha1BaseBlock.class);

        assertThat(block.getId()).isEqualTo("123449cf-7c83-49bf-aa3b-624b03d454a9");
        assertThat(block.getType()).isEqualTo("paragraph");
        assertThat(block.getProps()).isOfAnyClassIn(V1alpha1DefaultProps.class);
        assertThat(block.getProps().getTextColor()).isEqualTo("default");
        assertThat(block.getProps().getBackgroundColor()).isEqualTo("default");
        assertThat(block.getProps().getTextAlignment()).isEqualTo("left");
        assertThat(block.getChildren()).hasSize(0);

        V1alpha1InlineContent inline = block.getContent().getFirst();
        assertThat(inline.getType()).isEqualTo("text");
        assertThat(inline.getText()).isEqualTo("Welcome to this demo!");

    }


    @Test
    void complexBlockNodeJsonTest() throws Exception {
        String jsonString = """
                [
                  {
                    "id": "22ab8597-47c7-40df-88dc-16a308a2b826",
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
                        "text": "This is heading",
                        "styles": {}
                      }
                    ],
                    "children": []
                  },
                  {
                    "id": "b48f141e-4704-4b73-acf0-ca54c465d84c",
                    "type": "numberedListItem",
                    "props": {
                      "textColor": "default",
                      "backgroundColor": "default",
                      "textAlignment": "left"
                    },
                    "content": [
                      {
                        "type": "text",
                        "text": "numberedList",
                        "styles": {}
                      }
                    ],
                    "children": [
                      {
                        "id": "51f3db79-4f54-4964-b05e-acb2cfcb21dd",
                        "type": "numberedListItem",
                        "props": {
                          "textColor": "default",
                          "backgroundColor": "default",
                          "textAlignment": "left"
                        },
                        "content": [
                          {
                            "type": "text",
                            "text": "Nested Numbered List1",
                            "styles": {}
                          }
                        ],
                        "children": []
                      },
                      {
                        "id": "779d4f1b-6e67-4ee3-ac3f-a89631d1b11b",
                        "type": "numberedListItem",
                        "props": {
                          "textColor": "default",
                          "backgroundColor": "default",
                          "textAlignment": "left"
                        },
                        "content": [
                          {
                            "type": "text",
                            "text": "Nested Numbered List2",
                            "styles": {}
                          }
                        ],
                        "children": []
                      }
                    ]
                  },
                  {
                    "id": "bee14318-604f-4d8c-b715-db77540afe2c",
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
        ObjectMapper mapper = new ObjectMapper();

    }

}
