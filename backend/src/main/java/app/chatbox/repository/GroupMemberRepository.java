package app.chatbox.repository;

import app.chatbox.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    @Query(value = """
    SELECT gm.user_id
    FROM group_member gm
    WHERE gm.group_id = :groupId
    """, nativeQuery = true)
    List<Long> findAllMemberIds(Long groupId);

}
