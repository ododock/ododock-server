package ododock.webserver.domain.article;

import io.netty.util.internal.StringUtil;
import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import ododock.webserver.domain.article.dto.V1alpha1InlineContent;

import java.util.List;

public abstract class ArticleExcerptor {

    private static final int MAX_LENGTH = 200;

    public static String from(List<V1alpha1BaseBlock> body) {
        if (body == null || body.isEmpty()) {
            return StringUtil.EMPTY_STRING;
        }
        StringBuffer sb = new StringBuffer();
        iterateBlocks(sb, body);
        return sb.substring(0, MAX_LENGTH);
    }

    private static void iterateBlocks(StringBuffer sb, List<V1alpha1BaseBlock> blocks) {
        if (sb.length() >= MAX_LENGTH) {
            return;
        }
        for (V1alpha1BaseBlock block : blocks) {
            iterateInlineContents(sb, block.getContent());
            if (BlockUtils.hasChildrenContent(block)) {
                iterateBlocks(sb, block.getChildren());
            }
        }
    }

    private static void iterateInlineContents(StringBuffer sb, List<V1alpha1InlineContent> contents) {
        for (V1alpha1InlineContent content : contents) {
            if (BlockUtils.hasTextContent(content)) {
                sb.append(content.getText());
                sb.append(" ");
            }
        }
    }

}
