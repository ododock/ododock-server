package ododock.webserver.domain.article;

import com.mongodb.lang.Nullable;
import io.netty.util.internal.StringUtil;
import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import ododock.webserver.domain.article.dto.V1alpha1InlineContent;

import java.util.List;

public abstract class ArticleExcerptor {

    public static String from(List<V1alpha1BaseBlock> body, @Nullable Integer maxLength) {
        if (body == null || body.isEmpty()) {
            return StringUtil.EMPTY_STRING;
        }
        StringBuffer sb = new StringBuffer();

        iterateBlocks(sb, body, maxLength);
        if (maxLength != null && sb.length() > maxLength) {
            return sb.substring(0, maxLength);
        }
        return sb.toString();
    }

    private static void iterateBlocks(StringBuffer sb, List<V1alpha1BaseBlock> blocks, @Nullable Integer maxLength) {
        if (maxLength != null && sb.length() >= maxLength) {
            return;
        }
        for (V1alpha1BaseBlock block : blocks) {
            iterateInlineContents(sb, block.getContent());
            if (BlockUtils.hasChildrenContent(block)) {
                iterateBlocks(sb, block.getChildren(), maxLength);
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
