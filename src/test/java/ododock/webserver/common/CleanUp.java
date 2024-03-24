package ododock.webserver.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class CleanUp {

    private final JdbcTemplate jdbcTemplate;

    private final EntityManager em;

    public CleanUp(final JdbcTemplate jdbcTemplate, final EntityManager em) {
        this.jdbcTemplate = jdbcTemplate;
        this.em = em;
    }

    @Transactional
    public void all() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        List<String> tables = new ArrayList<>();
        em.getMetamodel().getEntities().forEach(entityType -> {
            final String tableName = entityType.getJavaType().getAnnotation(Table.class).name();
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
        });
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
