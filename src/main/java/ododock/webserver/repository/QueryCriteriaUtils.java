package ododock.webserver.repository;

import ododock.webserver.domain.article.ArticleListOptions;
import ododock.webserver.domain.article.Tag;
import ododock.webserver.web.InvalidParameterException;
import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

public abstract class QueryCriteriaUtils {

    private static final String SORT_KEY_DELIMITER = "=";

    public static Query queryWith(ArticleListOptions listOptions) throws BadRequestException {
        Query query = new Query();

        if (listOptions.getSize() != null) {
            query.limit(listOptions.getSize());
        }

        if (listOptions.getSort() != null && !listOptions.getSort().isBlank()) {
            Map<String, Sort.Direction> sortParams = CursorOptionUtils.parse(listOptions.getSort());
            List<Sort.Order> sortList = sortParams.entrySet()
                    .stream()
                    .map(QueryCriteriaUtils::toSortOrder)
                    .toList();
            query.with(Sort.by(sortList.toArray(new Sort.Order[0])));
        } else {

        }

        if (listOptions.getSortKeys() != null && !listOptions.getSortKeys().isBlank()) {
            Map<String, String> cursorValues = parse(listOptions.getSortKeys());
            Map<String, Sort.Direction> sortDirections = CursorOptionUtils.parse(listOptions.getSort());

            Criteria cursorCriteria = buildCursorCriteria(cursorValues, sortDirections);
            query.addCriteria(cursorCriteria);
        }

        if (listOptions.getAuthorName() != null) {
            Criteria criteria = Criteria.where("authorName").is(listOptions.getAuthorName());
            query.addCriteria(criteria);
        }

        if (listOptions.getVisibility() != null) {
            Criteria criteria = Criteria.where("visibility").is(listOptions.getVisibility());
            query.addCriteria(criteria);
        }

        if (listOptions.getTitle() != null && !listOptions.getTitle().isEmpty()) {
            Criteria criteria = Criteria.where("title").is(listOptions.getTitle());
            query.addCriteria(criteria);
        }

        if (listOptions.getKeyword() != null && !listOptions.getKeyword().isBlank()) {
            String keyword = listOptions.getKeyword().trim();
            String[] words = keyword.split("\\s+");

            List<Criteria> keywordRegexCriteria = Arrays.stream(words)
                    .map(word -> Criteria.where("plainText")
                            .regex(".*" + Pattern.quote(word) + ".*"))
                    .toList();

            query.addCriteria(new Criteria().andOperator(keywordRegexCriteria));
        }

        if (listOptions.getCategoryId() != null) {
            Criteria criteria = Criteria.where("categoryId").is(listOptions.getCategoryId());
            query.addCriteria(criteria);
        }

        if (listOptions.getTags() != null && !listOptions.getTags().isEmpty()) {
            List<Tag> tags = listOptions.getTags()
                    .stream().map(Tag::new).toList();

            List<Criteria> test = tags.stream().map(t -> Criteria.where("tags").is(t)).toList();

            Criteria criteria = new Criteria().andOperator(test);
            query.addCriteria(criteria);
        }

        return query;
    }

    private static Sort.Order toSortOrder(Map.Entry<String, Sort.Direction> entry) {
        if (entry.getValue() == Sort.Direction.ASC) {
            return Sort.Order.asc(entry.getKey());
        }
        return Sort.Order.desc(entry.getKey());
    }

    private static Map<String, String> parse(String param) {
        Set<String> sortKeys = StringUtils.commaDelimitedListToSet(param);

        HashMap<String, String> tokenMap = new HashMap<>();
        sortKeys.forEach(sortKey -> {
            if (!sortKey.contains(SORT_KEY_DELIMITER)) {
                throw new InvalidParameterException("sortKeys", "Invalid request parameter");
            }
            String[] token = sortKey.split(SORT_KEY_DELIMITER);
            tokenMap.put(token[0], token[1]);
        });

        return tokenMap;
    }

    private static Criteria buildCursorCriteria(Map<String, String> cursorValues, Map<String, Sort.Direction> sortDirections) {
        if (!cursorValues.keySet().equals(sortDirections.keySet())) {
            throw new InvalidParameterException("sortKeys", "Invalid parameter. sortKeys parameter must be match with sort parameters");
        }

        List<Criteria> orCriteriaList = new ArrayList<>();
        List<String> keys = new ArrayList<>(sortDirections.keySet());

        // __ todo refactor to stream
        for (int i = 0; i < keys.size(); i++) {
            List<Criteria> andConditions = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                String prevKey = keys.get(j);
                andConditions.add(Criteria.where(prevKey).is(cursorValues.get(prevKey)));
            }

            String currentKey = keys.get(i);
            Object currentValue = convertTypedValue(currentKey, cursorValues.get(currentKey));
            Sort.Direction dir = sortDirections.get(currentKey);

            if (dir == Sort.Direction.ASC) {
                andConditions.add(Criteria.where(currentKey).gt(currentValue));
            } else {
                andConditions.add(Criteria.where(currentKey).lt(currentValue));
            }

            orCriteriaList.add(new Criteria().andOperator(andConditions.toArray(new Criteria[0])));
        }

        return new Criteria().orOperator(orCriteriaList.toArray(new Criteria[0]));
    }

    private static Object convertTypedValue(String key, String value) {
        switch (key) {
            case "id":
            case "_id":
                return new ObjectId(value); // type casting to  ObjectId
            case "createdAt":
            case "updatedAt":
                Date converted = null;
                try {
                    converted = Date.from(Instant.parse(value));
                } catch (DateTimeParseException ex) {
                    throw new InvalidParameterException("sortKey", "Invalid date format");
                }
                return converted; // String to ISODate
            default:
                return value;
        }
    }

}
