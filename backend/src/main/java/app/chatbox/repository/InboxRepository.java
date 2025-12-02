package app.chatbox.repository;

import app.chatbox.model.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, Long> {
    List<Inbox> findByUserA_IdOrUserB_Id(Long userAId, Long userBId);

    @Query(value = """
    select
        case
            when i.userA = :userId then i.userB
            else i.userA
        end as friend_id,

        case
            when i.userA = :userId then u2.username
            else u1.username
        end as username,

        (
            select m.content
            from inbox_message m
            where m.inbox_id = i.id
            order by m.created_at desc
            limit 1
        ) as last_msg

    from inbox i
    join app_user u1 on i.userA = u1.id
    join app_user u2 on i.userB = u2.id
    where i.userA = :userId or i.userB = :userId
    """, nativeQuery = true)
    List<Object[]> findFriendsWithLastMsg(Long userId);

}
