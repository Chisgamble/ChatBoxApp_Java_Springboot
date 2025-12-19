package app.chatbox.repository;

import app.chatbox.dto.AddMemberCardDTO;
import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.GroupCardDTO;
import app.chatbox.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(
            value = """
            SELECT
                g.id              AS id,
                gm.sender_id      AS senderId,
                g.group_name      AS groupname,
                gm.content        AS last_msg
            FROM group_member m
            JOIN chat_group g
                ON m.group_id = g.id
            LEFT JOIN group_message gm
                ON g.last_msg = gm.id
            WHERE m.user_id = :userId
            ORDER BY g.updated_at DESC
        """, nativeQuery = true)
    List<GroupCardDTO> getAllCardByUserId(Long userId);

    @Query(value = """
            SELECT u.id, u.username
            FROM friend f
            JOIN app_user u
              ON u.id = CASE
                  WHEN f.userA = :currentUserId THEN f.userB
                  ELSE f.userA
              END
            WHERE (f.userA = :currentUserId OR f.userB = :currentUserId)
              AND u.is_locked = false
              AND u.id NOT IN (
                  SELECT gm.user_id
                  FROM group_member gm
                  WHERE gm.group_id = :groupId
              );
            
            """, nativeQuery = true)
    List<AddMemberCardDTO> getAllFriendsNotInGroup(Long groupId, Long currentUserId);

    @Query(value = """
        SELECT
            g.id,
            g.group_name,
            g.created_at
        FROM chat_group g
        WHERE
            (:groupNameRegex IS NULL OR CAST(g.group_name AS text) ~*  LOWER(:groupNameRegex))
        AND
            (:startDate IS NULL OR g.created_at >= CAST(:startDate AS timestamp))
        AND
            (:endDate IS NULL OR g.created_at <= CAST(:endDate AS timestamp))
        ORDER BY
            CASE WHEN :sortBy = 'name' AND :sortDir = 'asc' THEN g.group_name END ASC,
            CASE WHEN :sortBy = 'name' AND :sortDir = 'desc' THEN g.group_name END DESC,
            CASE WHEN :sortBy = 'time' AND :sortDir = 'asc' THEN g.created_at END DESC,
            CASE WHEN :sortBy = 'time' AND :sortDir = 'desc' THEN g.created_at END ASC
    """, nativeQuery = true)
    List<Object[]> findGroupListDataRaw(
            @Param("groupNameRegex") String groupNameRegex,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir
    );

}
