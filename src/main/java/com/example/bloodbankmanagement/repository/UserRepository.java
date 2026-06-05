package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.dto.objectRepository.AssignmentTotalByAdmissionPeriodDto;
import com.example.bloodbankmanagement.dto.objectRepository.CountTotalRecordDto;
import com.example.bloodbankmanagement.dto.objectRepository.InstructorTotalByYearDto;
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
            "and u.id not in (select instructor_id from student_map_instructor where student_id = ?2)", nativeQuery = true)
    List<UserInfoDto> getListCriticalByStudentId(String valueRole, Long studentId);


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


    @Query("select u from User u where u.status = ?1 and u.statusSendMail = ?2 ")
    List<User> getListAccountNotSendMailBefore(String statusActiveAccount, String isSendMailSuccess);

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

    @Modifying
    @Transactional
    @Query(value = "update users set status_send_mail = ?1  where id = ?2",nativeQuery = true)
    void updateStatusSendMailSuccess(String statusSendMailSuccess, Long valueUserId);


    @Modifying
    @Transactional
    @Query(value = "update users set status_send_mail = ?1  where id in ?2",nativeQuery = true)
    void updateListAccountStatusSendMailSuccess(String statusSendMailSuccess, List<Long> userId);

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
            "and (:#{#request.email} is null or ''  = :#{#request.email} or u.email like '%'+:#{#request.email}+'%') " +
            "and (:#{#request.phone} is null or ''  = :#{#request.phone} or u.phone like '%'+:#{#request.phone}+'%') " +
            "and (:#{#request.fullName} is null or ''  = :#{#request.fullName} or u.full_name like '%'+:#{#request.fullName}+'%') " +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or u.status = :#{#request.status}) " +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or u.create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or u.create_at <= :#{#request.toDate}) " +
            "order by u.create_at DESC, u.update_at DESC ",
            nativeQuery = true)
    Page<User> findListStudent(@Param("request") StudentManagementDto.StudentSelectListRequest request, Pageable pageable);

    @Query(value = "select count(id) as totalValue, 'TOTAL_STUDENT' as totalName  from student " +
            " union all " +
            " select count(id) as totalValue, 'TOTAL_ASSIGNMENT' as totalName  from assignment_student_register " +
            " union all " +
            " select count(id) as totalValue, 'TOTAL_FILE_UPLOAD' as totalName  from file_meta_data ", nativeQuery = true)
    List<CountTotalRecordDto> findAllRecordRelateStudent();


    @Query(value = "with totalAssignmentAdmission as ( " +
            " select ap.id as admissionId, count(ap.id) as totalAssignemnt " +
            " from assignment_student_register asr " +
            " join period_assignment pa on asr.period_assignment_id = pa.id " +
            " join admission_period ap on pa.admission_period_id = ap.id " +
            " group by ap.id) " +
            " select ap.id as admissionPeriodId, ap.admission_period_name as admissionPeriodName ,tat.totalAssignemnt as totalAssignment from admission_period ap " +
            " left join totalAssignmentAdmission tat on ap.id = tat.admissionId " +
            " where tat.totalAssignemnt is not null and tat.totalAssignemnt > 0 " +
            " order by tat.totalAssignemnt DESC ", nativeQuery = true)
    List<AssignmentTotalByAdmissionPeriodDto> getTop5PeriodHaveModeAssignmentRegister();




    @Query(value = "with totalInstructorHaveMaxStudentByYear as ( " +
            " select instructor_id, count(instructor_id) as totalStudent  " +
            " from student_map_instructor " +
            " where instructor_id is not null " +
            " and create_at > ?1 " +
            " and create_at < ?2" +
            " group by instructor_id) " +
            " select tat.instructor_id as instructorId, user_info.full_name as instructorName ,tat.totalStudent as totalStudent " +
            " from totalInstructorHaveMaxStudentByYear tat " +
            " left join users user_info on tat.instructor_id = user_info.id " +
            " order by tat.totalStudent DESC ", nativeQuery = true)
    List<InstructorTotalByYearDto> getTop5InstructorHaveMaxStudentAssignByYear(String startDate, String endDate);
}
