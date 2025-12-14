package app.chatbox.repository;

import app.chatbox.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroup_IdAndUser_Id(Long groupId, Long userId);

    @Query(value = """
    SELECT gm.user_id
    FROM group_member gm
    WHERE gm.group_id = :groupId
    """, nativeQuery = true)
    List<Long> findAllMemberIds(Long groupId);



}
