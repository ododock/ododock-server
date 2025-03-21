package ododock.webserver.domain.article.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Data
public class V1alpha1InlineContent {

    String type;
    String text;
    V1alpha1Styles styles;

    @Nullable
    List<Long> columnWidths;
    @Nullable
    List<V1alpha1TableRow> rows;

    @Data
    @EqualsAndHashCode
    private static class V1alpha1TableRow {
        List<V1alpha1TableCell> cells;

        public V1alpha1TableRow() {
            this.cells = new ArrayList<>();
        }
    }

    @Data
    @EqualsAndHashCode
    private static class V1alpha1TableCell {
        String type = "tableCell";
        List<List<? extends V1alpha1InlineContent>> content;
        V1alpha1TableCellProps props;

        public V1alpha1TableCell() {
            this.content = new ArrayList<>(new ArrayList<>());
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    private static class V1alpha1TableCellProps extends V1alpha1DefaultProps {
        Long colspan;
        Long rowspan;
    }

}
