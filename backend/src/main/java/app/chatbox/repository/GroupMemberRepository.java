package app.chatbox.repository;

import app.chatbox.dto.GroupMemberDTO;
import app.chatbox.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroup_IdAndUser_Id(Long groupId, Long userId);
    boolean existsByGroup_IdAndUser_Id(Long groupId, Long newUserId);

    @Query(value = """
    SELECT gm.user_id
    FROM group_member gm
    WHERE gm.group_id = :groupId
    """, nativeQuery = true)
    List<Long> findAllMemberIds(Long groupId);

    @Query(
            value = """
            SELECT
                gm.id           AS id,
                u.id            AS userId,
                gm.group_id     AS groupId,
                u.email         AS email,
                u.username      AS username,
                gm.role         AS role
            FROM group_member gm
            JOIN app_user u ON u.id = gm.user_id
            WHERE gm.group_id = :groupId
            ORDER BY gm.created_at ASC
        """,
            nativeQuery = true
    )
    List<GroupMemberDTO> findAllMembersByGroupId(Long groupId);

//    @Query(
//            value = """
//            DELETE FROM group_member
//            WHERE group_id = :groupId AND user_id = :userId
//        """, nativeQuery = true
//    )
//    void leaveGroup(Long groupId, Long userId);

    void deleteByGroup_IdAndUser_Id(Long groupId, Long UserId);

}
