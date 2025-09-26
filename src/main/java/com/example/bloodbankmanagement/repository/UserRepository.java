package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from Users " +
            "where (:#{#request.id} is null or ''  = :#{#request.id} or id like :#{#request.id})" +
            "and (:#{#request.username} is null or ''  = :#{#request.username} or username like '%'+:#{#request.username}+'%')" +
            "and (:#{#request.email} is null or ''  = :#{#request.email} or email like '%'+:#{#request.email}+'%')" +
            "and (:#{#request.phone} is null or ''  = :#{#request.phone} or phone like '%'+:#{#request.phone}+'%')" +
            "and (:#{#request.identityCard} is null or ''  = :#{#request.identityCard} or identity_card like '%'+:#{#request.identityCard}+'%')" +
            "and (:#{#request.fullName} is null or ''  = :#{#request.fullName} or full_name like '%'+:#{#request.fullName}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status}) " +
            " order by create_at DESC,create_tm DESC,update_at DESC,update_tm DESC ",
            nativeQuery = true)
    Page<User> findListUsers(@Param("request") UserDto.UserSelectListRequest request, Pageable pageable);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("select u from User u where u.id = ?1")
    User getById(Long id);

    @Query("select u from User u where u.email = ?1 order by u.createAt desc, u.createTm desc")
    List<User> getByEmail(String email);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "update Users set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}, update_tm =:#{#request.updateTm} where id =:#{#request.id}",nativeQuery = true)
    void updateStatusById(@Param("request") User request);

    @Modifying
    @Transactional
    @Query(value = "update Users set  username =:#{#request.username}, email =:#{#request.email}, phone =:#{#request.phone},full_name =:#{#request.fullName},identity_card =:#{#request.identityCard},address =:#{#request.address}," +
            "status =:#{#request.status},note =:#{#request.note},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt},update_tm =:#{#request.updateTm}" +
            " where id =:#{#request.id}",nativeQuery = true)
    void updateUser(@Param("request") User request);

    @Modifying
    @Transactional
    @Query(value = "update Users set password =:#{#request.password},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}, update_tm =:#{#request.updateTm} where id =:#{#request.id}",nativeQuery = true)
    void changePassword(@Param("request") User request);

    List<User> findAllByIdIn(ArrayList<Long> userId);
}
