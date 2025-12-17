package app.chatbox.repository;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.response.StrangerCardResDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUserA_IdOrUserB_Id(Long userAId, Long userBId);
    Optional<Friend> findByUserA_IdAndUserB_Id(Long userAId, Long userBId);
    Optional<Friend> findByUserAAndUserB(AppUser userA, AppUser userB);

    void deleteById(Long id);
    void deleteByUserA_IdAndUserB_Id(Long userAId, Long userBId);

    @Query(value = """
        SELECT 
            f.id AS id,
            u.id AS friendId,
            COALESCE(i.id, 0) AS inboxId,
            u.username AS username,
            u.is_active AS isActive,
            COALESCE(m.sender_id, 0) AS senderId,
            m.content AS content
        FROM friend f
        JOIN app_user u 
            ON (CASE 
                    WHEN f.userA = :currentUserId THEN f.userB 
                    ELSE f.userA 
                END) = u.id
            AND u.is_locked = false
        LEFT JOIN inbox i 
            ON (
                (i.userA = :currentUserId AND i.userB = u.id) OR
                (i.userA = u.id AND i.userB = :currentUserId)
            )
        LEFT JOIN LATERAL (
            SELECT im.sender_id, im.content
            FROM inbox_message im
            WHERE im.inbox_id = i.id
            ORDER BY im.created_at DESC
            LIMIT 1
        ) m ON TRUE
        WHERE f.userA = :currentUserId OR f.userB = :currentUserId
        ORDER BY f.updated_at DESC;
    """, nativeQuery = true)
    List<FriendCardDTO> getFriendCards(long currentUserId);



    @Query(value = """
    SELECT * FROM (
        SELECT 
            u.username AS username,
            u.created_at AS createdAt,
            
            -- 1. Calculate Friend Count (Direct connections)
            (SELECT COUNT(*) 
             FROM friend f 
             WHERE f.usera = u.id OR f.userb = u.id
            ) AS friendCount,
            
            -- 2. Calculate FoF Count (Neighbors of Neighbors, Distinct, Exclude Self AND Direct Friends)
            (SELECT COUNT(DISTINCT fof.id)
             FROM friend f1 -- Relationships of the current user (u.id)
             JOIN app_user direct_friend 
                ON (CASE WHEN f1.usera = u.id THEN f1.userb ELSE f1.usera END) = direct_friend.id
             JOIN friend f2 -- Relationships of the direct friend (direct_friend.id)
                ON (f2.usera = direct_friend.id OR f2.userb = direct_friend.id)
             JOIN app_user fof -- The actual FoF user
                ON (CASE WHEN f2.usera = direct_friend.id THEN f2.userb ELSE f2.usera END) = fof.id
             
             WHERE (f1.usera = u.id OR f1.userb = u.id) -- Filter f1 to only relate to the current user (u.id)
               AND fof.id != u.id                      -- EXCLUSION 1: Cannot be the user themselves
               AND fof.id != direct_friend.id          -- EXCLUSION 2: Cannot be the direct friend (A is not B)
               
               -- EXCLUSION 3 (CRITICAL): Ensure the FoF (fof.id) is NOT already a direct friend of u.id
               -- This is the most complete way to ensure a true FoF
               AND NOT EXISTS (
                   SELECT 1 
                   FROM friend f3 
                   WHERE (f3.usera = u.id AND f3.userb = fof.id) 
                      OR (f3.usera = fof.id AND f3.userb = u.id)
               )
            ) AS fofCount
        
        FROM app_user u
    ) AS stats
    
    WHERE (:usernamePattern IS NULL OR stats.username LIKE :usernamePattern)
    
    -- Filter by Friend Count using logic gates on the calculated value
    AND (
        (:symbol IS NULL OR :val IS NULL)
        OR (:symbol = '>' AND stats.friendCount > :val)
        OR (:symbol = '<' AND stats.friendCount < :val)
        OR (:symbol = '=' AND stats.friendCount = :val)
    )
        
    ORDER BY
        CASE WHEN :sortBy = 'username' AND :sortDir = 'asc' THEN stats.username END ASC,
        CASE WHEN :sortBy = 'username' AND :sortDir = 'desc' THEN stats.username END DESC,
        CASE WHEN :sortBy = 'accountAge' AND :sortDir = 'asc' THEN stats.createdAt END DESC, 
        CASE WHEN :sortBy = 'accountAge' AND :sortDir = 'desc' THEN stats.createdAt END ASC
""", nativeQuery = true)
    List<Object[]> findFriendListDataRaw(
            @Param("usernamePattern") String usernamePattern,
            @Param("symbol") String symbol,
            @Param("val") Integer val,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir
    );
}
