package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.dto.objectRepository.UserInfoDto;
import com.example.bloodbankmanagement.dto.service.InstructorDto;
import com.example.bloodbankmanagement.dto.service.StudentManagementDto;
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
            "join roles r on r.id = u.role_id " +
            "where u.id = ?1 and r.name = ?2", nativeQuery = true)
    User getValueUserByIdAndRole(Long id, String valueRole);

    @Query(value = "select u.*  from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = ?1 and u.status ='1' ", nativeQuery = true)
    List<User> getListUserByRoleName(String valueRole);

    @Query(value = "with StudentCTE AS(" +
            "select u.id, u.full_name  from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = ?1 and u.status ='1') " +
            "         select" +
            "          smi.student_id as id," +
            "          case when smi.instructor_id is null then '' " +
            "          when smi.instructor_id is not null then scte.full_name " +
            "          else '' end as fullName "+
            "  from student_map_instructor smi " +
            " join StudentCTE scte  on scte.id = smi.student_id " +
            "where smi.instructor_id = ?2 and smi.status ='1' ", nativeQuery = true)
    List<UserInfoDto> getListUserByRoleNameMapWithInstructorId(String valueRole, Long intructorId);

    @Query(value = "with StudentCTE AS(" +
            "select u.id, u.full_name  from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = ?1 and u.status ='1') " +
            "         select" +
            "          smi.student_id as id," +
            "          case when smi.instructor_id is null then '' " +
            "          when smi.instructor_id is not null then scte.full_name " +
            "          else '' end as fullName "+
            "  from student_map_instructor smi " +
            " join StudentCTE scte  on scte.id = smi.student_id " +
            "where smi.instructor_id = ?2 and smi.status ='1' " +
            "and scte.id not in (select student_id from assignment_student_register where is_approved <> ?3 or status <> ?4 ) ", nativeQuery = true)
    List<UserInfoDto> getListStudentMapWithInstructorButNotRegisterAssignment(String valueRole, Long intructorId, Integer isApproveIsReserve, String statusDelete);

    @Query(value = "select u.id as id, u.full_name as fullName from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = ?1 and u.status ='1' " +
            "and u.id not in (select student_id from student_map_instructor where instructor_id = ?2)", nativeQuery = true)
    List<UserInfoDto> getListUserNotMapInstructor(String valueRole, Long intructorId);


    @Query(value = "select u.id as id, u.full_name as fullName from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = ?1 and u.status ='1' " +
            "and u.id not in (select student_id from assignment_student_register where student_id = ?2 and  (is_approved <> ?3 or status <> ?4 ))", nativeQuery = true)
    List<UserInfoDto> getListUserNotRegisterAssignment(String valueRole, Long studentId, Integer isApproveIsReserve, String statusDelete);


    @Query(value = "select u.id as id, u.full_name as fullName from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = ?1 and u.status ='1' " +
            "and u.id in (select student_id from assignment_student_register where student_id = ?2 and  (is_approved <> ?3 or status <> ?4 ))", nativeQuery = true)
    List<UserInfoDto> getListUserRegisterAssignment(String valueRole, Long studentId, Integer isApproveIsReserve, String statusDelete);

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
            "join roles r on r.id = u.role_id " +
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
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<User> findListStudents(@Param("request") StudentDto.StudentSelectListRequest request, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update Users set  username =:#{#request.username}, email =:#{#request.email}, phone =:#{#request.phone},full_name =:#{#request.fullName},identity_card =:#{#request.identityCard},address =:#{#request.address}," +
            " note =:#{#request.note},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}" +
            " where id =:#{#request.id}",nativeQuery = true)
    void updateStudent(@Param("request") User request);
    @Modifying
    @Transactional
    @Query(value = "update Users set  username =:#{#request.username}, email =:#{#request.email}, phone =:#{#request.phone},full_name =:#{#request.fullName},identity_card =:#{#request.identityCard},address =:#{#request.address}," +
            " note =:#{#request.note},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt}" +
            " where id =:#{#request.id}",nativeQuery = true)
    void updateTeacher(@Param("request") User request);

    @Modifying
    @Transactional
    @Query(value = "select * from Users WHERE status = '1' and username in :ids",nativeQuery = true)
    List<User> getListUsersActiveByUserName(@Param("ids") List<String> ids);

    @Query(value = "select u.* from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = 'ROLE_INSTRUCTOR' " +
            "and (:#{#request.instructorId} is null or ''  = :#{#request.instructorId} or u.id like :#{#request.instructorId}) " +
            "and (:#{#request.username} is null or ''  = :#{#request.username} or u.username like '%'+:#{#request.username}+'%') " +
            "and (:#{#request.email} is null or ''  = :#{#request.email} or u.email like '%'+:#{#request.email}+'%') " +
            "and (:#{#request.phone} is null or ''  = :#{#request.phone} or u.phone like '%'+:#{#request.phone}+'%') " +
            "and (:#{#request.fullName} is null or ''  = :#{#request.fullName} or u.full_name like '%'+:#{#request.fullName}+'%') " +
            "and (:#{#request.identityCard} is null or ''  = :#{#request.identityCard} or u.identity_card like '%'+:#{#request.identityCard}+'%') " +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or u.status = :#{#request.status}) " +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or u.create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or u.create_at <= :#{#request.toDate}) " +
            "order by u.create_at DESC, u.update_at DESC ",
            nativeQuery = true)
    Page<User> findListInstructor(@Param("request") InstructorDto.InstructorSelectListRequest request, Pageable pageable);

    @Query(value = "select u.* from users u " +
            "join roles r on r.id = u.role_id " +
            "where r.name = 'ROLE_USER' " +
            "and (:#{#request.studentId} is null or ''  = :#{#request.studentId} or u.id like :#{#request.studentId}) " +
            "and (:#{#request.username} is null or ''  = :#{#request.username} or u.username like '%'+:#{#request.username}+'%') " +
            "and (:#{#request.email} is null or ''  = :#{#request.email} or u.email like '%'+:#{#request.email}+'%') " +
            "and (:#{#request.phone} is null or ''  = :#{#request.phone} or u.phone like '%'+:#{#request.phone}+'%') " +
            "and (:#{#request.fullName} is null or ''  = :#{#request.fullName} or u.full_name like '%'+:#{#request.fullName}+'%') " +
            "and (:#{#request.identityCard} is null or ''  = :#{#request.identityCard} or u.identity_card like '%'+:#{#request.identityCard}+'%') " +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or u.status = :#{#request.status}) " +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or u.create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or u.create_at <= :#{#request.toDate}) " +
            "order by u.create_at DESC, u.update_at DESC ",
            nativeQuery = true)
    Page<User> findListStudent(@Param("request") StudentManagementDto.StudentSelectListRequest request, Pageable pageable);
}
