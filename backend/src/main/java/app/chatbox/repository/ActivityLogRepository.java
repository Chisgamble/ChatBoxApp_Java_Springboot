package app.chatbox.repository;

import app.chatbox.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    @Query(value = """
        WITH UserActivity AS (
            SELECT
                u.id AS user_id,
                u.username,
                u.created_at AS account_created_at,
                -- Count total 'open' activities
                SUM(CASE WHEN al.action = 'open' THEN 1 ELSE 0 END) AS open_count,
                -- Count total 'chat_one' activities
                SUM(CASE WHEN al.action = 'chat_one' THEN 1 ELSE 0 END) AS chat_one_count,
                -- Count total 'chat_group' activities
                SUM(CASE WHEN al.action = 'chat_group' THEN 1 ELSE 0 END) AS chat_group_count
            FROM app_user u
            LEFT JOIN activity_log al ON u.id = al.user_id
            GROUP BY u.id, u.username, u.created_at
        )
        SELECT
            ua.user_id,
            ua.username,
            ua.account_created_at,
            ua.open_count,
            ua.chat_one_count,
            ua.chat_group_count
        FROM UserActivity ua
        WHERE
            -- Username filter
            (:usernamePattern IS NULL OR ua.username ILIKE :usernamePattern)
            AND
            -- Activity count filter logic (Safely handles comparison operators)
            (
                :activityType IS NULL OR :comparison IS NULL OR :count IS NULL
                OR
                (
                    (:activityType = 'open' AND
                        CASE :comparison
                            WHEN '<' THEN ua.open_count < CAST(:count AS bigint)
                            WHEN '>' THEN ua.open_count > CAST(:count AS bigint)
                            WHEN '=' THEN ua.open_count = CAST(:count AS bigint)
                            ELSE FALSE
                        END
                    )
                    OR
                    (:activityType = 'with-one' AND
                        CASE :comparison
                            WHEN '<' THEN ua.chat_one_count < CAST(:count AS bigint)
                            WHEN '>' THEN ua.chat_one_count > CAST(:count AS bigint)
                            WHEN '=' THEN ua.chat_one_count = CAST(:count AS bigint)
                            ELSE FALSE
                        END
                    )
                    OR
                    (:activityType = 'with-group' AND
                        CASE :comparison
                            WHEN '<' THEN ua.chat_group_count < CAST(:count AS bigint)
                            WHEN '>' THEN ua.chat_group_count > CAST(:count AS bigint)
                            WHEN '=' THEN ua.chat_group_count = CAST(:count AS bigint)
                            ELSE FALSE
                        END
                    )
                )
            )
        ORDER BY
            CASE WHEN :sortBy = 'username' AND :sortDir = 'asc' THEN ua.username END ASC,
            CASE WHEN :sortBy = 'username' AND :sortDir = 'desc' THEN ua.username END DESC,
            -- Account Age (created_at) -> Sorting by time ASC or DESC
            CASE WHEN :sortBy = 'age' AND :sortDir = 'asc' THEN ua.account_created_at END DESC,
            CASE WHEN :sortBy = 'age' AND :sortDir = 'desc' THEN ua.account_created_at END ASC
        """, nativeQuery = true)
    List<Object[]> findUserActivityDataRaw(
            @Param("usernamePattern") String usernamePattern,
            @Param("activityType") String activityType,
            @Param("comparison") String comparison,
            @Param("count") String count,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir
    );
}