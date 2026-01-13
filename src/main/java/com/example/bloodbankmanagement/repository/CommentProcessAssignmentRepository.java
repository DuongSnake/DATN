package com.example.bloodbankmanagement.repository;

import com.example.bloodbankmanagement.dto.service.CommentProcessAssignmentDto;
import com.example.bloodbankmanagement.entity.CommentProcessAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentProcessAssignmentRepository extends JpaRepository<CommentProcessAssignment, Long> {
    @Query(value = "select *  from comment_process_assignment " +
            "where (:#{#request.commentProcessAssignmentId} is null or ''  = :#{#request.commentProcessAssignmentId} or id like :#{#request.commentProcessAssignmentId})" +
            "and (:#{#request.note} is null or ''  = :#{#request.note} or note like '%'+:#{#request.note}+'%')" +
            "and (:#{#request.status} is null or ''  = :#{#request.status} or status = :#{#request.status})" +
            "and (:#{#request.fileUploadId} is null or ''  = :#{#request.fileUploadId} or file_upload_id = :#{#request.fileUploadId})" +
            "and (:#{#request.fromDate} is null or ''  = :#{#request.fromDate} or create_at >= :#{#request.fromDate}) " +
            "and (:#{#request.toDate} is null or ''  = :#{#request.toDate} or create_at <= :#{#request.toDate}) " +
            " order by create_at DESC,update_at DESC ",
            nativeQuery = true)
    Page<CommentProcessAssignment> findListCommentProcessAssignment(CommentProcessAssignmentDto.CommentProcessAssignmentSelectListInfo request, Pageable pageable);

    @Query(value = "select * from comment_process_assignment where id = ?1",nativeQuery = true)
    CommentProcessAssignment findByFileId(Long id);

    @Modifying
    @Transactional
    @Query(value = "update comment_process_assignment set file_upload_id =:#{#request.fileUploadInfo.id}, note =:#{#request.note},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id = :#{#request.id}",nativeQuery = true)
    void updateCommentProcessAssignment(@Param("request") CommentProcessAssignment request);

    @Modifying
    @Transactional
    @Query(value = "update comment_process_assignment set status =:#{#request.status},update_user =:#{#request.updateUser},update_at =:#{#request.updateAt} WHERE id in :ids",nativeQuery = true)
    void deleteCommentProcessAssignment(@Param("request") CommentProcessAssignment request, @Param("ids") List<Long> ids);
}
