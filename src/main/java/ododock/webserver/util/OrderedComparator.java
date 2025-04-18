package ododock.webserver.util;

import org.springframework.core.Ordered;

import java.util.Comparator;

public class OrderedComparator implements Comparator<Ordered> {
    @Override
    public int compare(Ordered o1, Ordered o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
