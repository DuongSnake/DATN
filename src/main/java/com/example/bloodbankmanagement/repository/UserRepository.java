package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import com.example.bloodbankmanagement.entity.StudentMapInstructor;
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
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<User> findListUsers(@Param("request") UserDto.UserSelectListRequest request, Pageable pageable);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("select u from User u where u.id = ?1")
    User getById(Long id);

    @Query(value = "select u.*  from users u " +
            "join user_roles ur  on u.id = ur.user_id " +
            "join roles r on r.id = ur.role_id " +
            "where u.id = ?1 and r.name = ?2", nativeQuery = true)
    User getValueUserByIdAndRole(Long id, String valueRole);

    @Query(value = "select u.*  from users u " +
            "join user_roles ur  on u.id = ur.user_id " +
            "join roles r on r.id = ur.role_id " +
            "where r.name = ?1 and u.status ='1' ", nativeQuery = true)
    List<User> getListUserByRoleName(String valueRole);

    @Query("select u from User u where u.email = ?1 order by u.createAt desc")
    List<User> getByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update Users set  username =:#{#request.username}, email =:#{#request.email}, phone =:#{#request.phone},full_name =:#{#request.fullName},identity_card =:#{#request.identityCard},address =:#{#request.address}," +
            "note =:#{#request.note},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}" +
            " where id =:#{#request.id}",nativeQuery = true)
    void updateUser(@Param("request") User request);

    @Modifying
    @Transactional
    @Query(value = "update Users set password =:#{#request.password},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} where id =:#{#request.id}",nativeQuery = true)
    void changePassword(@Param("request") User request);

    List<User> findAllByIdIn(ArrayList<Long> userId);

    @Query(value = "select count(u.id)  from users u " +
            "join user_roles ur  on u.id = ur.user_id " +
            "join roles r on r.id = ur.role_id " +
            "where u.id in ?1 and r.name = ?2", nativeQuery = true)
    Integer countListUerInRangeAndByTypeRole(List<Long> userId, String typeRole);

    @Modifying
    @Transactional
    @Query(value = "update users set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteUsers(@Param("request") User request, @Param("ids") List<Long> ids);



    @Query(value = "select * from Users " +
            "where (:#{#request.id} is null or ''  = :#{#request.id} or id like :#{#request.id})" +
            "and (:#{#request.username} is null or ''  = :#{#request.username} or username like '%'+:#{#request.username}+'%')" +
            "and (:#{#request.email} is null or ''  = :#{#request.email} or email like '%'+:#{#request.email}+'%')" +
            "and (:#{#request.phone} is null or ''  = :#{#request.phone} or phone like '%'+:#{#request.phone}+'%')" +
            "and (:#{#request.identityCard} is null or ''  = :#{#request.identityCard} or identity_card like '%'+:#{#request.identityCard}+'%')" +
            "and (:#{#request.fullName} is null or ''  = :#{#request.fullName} or full_name like '%'+:#{#request.fullName}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status}) " +
            "and (:#{#request.majorId} is null or ''  = :#{#request.majorId} or major_id = :#{#request.majorId}) " +
            "and (:#{#request.admissionPeriodId} is null or ''  = :#{#request.admissionPeriodId} or period_time_id = :#{#request.admissionPeriodId}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<User> findListStudents(@Param("request") StudentDto.StudentSelectListRequest request, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update Users set  username =:#{#request.username}, email =:#{#request.email}, phone =:#{#request.phone},full_name =:#{#request.fullName},identity_card =:#{#request.identityCard},address =:#{#request.address}," +
            " period_time_id =:#{#request.periodTime.id}, major_id =:#{#request.majorInfo.id}, note =:#{#request.note},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}" +
            " where id =:#{#request.id}",nativeQuery = true)
    void updateStudent(@Param("request") User request);
    @Modifying
    @Transactional
    @Query(value = "update Users set  username =:#{#request.username}, email =:#{#request.email}, phone =:#{#request.phone},full_name =:#{#request.fullName},identity_card =:#{#request.identityCard},address =:#{#request.address}," +
            " major_id =:#{#request.majorInfo.id}, note =:#{#request.note},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}" +
            " where id =:#{#request.id}",nativeQuery = true)
    void updateTeacher(@Param("request") User request);
}
