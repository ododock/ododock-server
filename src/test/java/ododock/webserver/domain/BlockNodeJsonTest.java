package ododock.webserver.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import ododock.webserver.domain.article.dto.V1alpha1DefaultProps;
import ododock.webserver.domain.article.dto.V1alpha1InlineContent;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

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
                    "id": "d957fb97-db32-41c7-bf05-449b1838bb5d",
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
                    "id": "a469bf7c-ff3e-47c4-b612-cedff90d7dec",
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
                    "id": "5b468a2d-23ff-4764-93c7-4f0ef0bae78b",
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
                    "id": "eba23025-e696-4d61-b668-252a05bf2b2a",
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
                    "id": "b072c8cb-70fe-4b37-a239-5a3d5f90999e",
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
                    "id": "0be83fae-1a57-47b0-b80e-984c058ab0bd",
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
                    "id": "e243ac50-8eb1-47ba-9dab-1bed1bcbc025",
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
                    "id": "29b56e7f-f334-4eab-aac8-30b271c5f073",
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
                    "id": "97b538d3-2a6c-41cd-995a-dd395739cf95",
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
        Type listType = new TypeToken<List<V1alpha1BaseBlock>>() {}.getType();
        List<V1alpha1BaseBlock> blocks = new Gson().fromJson(jsonString, listType);
        assertThat(blocks).isNotEmpty();
        assertThat(blocks.getFirst().getType()).isEqualTo("heading");
    }

}
