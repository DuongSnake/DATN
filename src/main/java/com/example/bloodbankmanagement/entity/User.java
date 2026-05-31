package com.example.bloodbankmanagement.entity;



import com.example.bloodbankmanagement.common.untils.AbstractEntity;
import com.example.bloodbankmanagement.common.untils.EntityCommon;
import com.example.bloodbankmanagement.dto.common.PageAmtListResponseDto;
import com.example.bloodbankmanagement.dto.objectRepository.UserInfoDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users" , uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@AllArgsConstructor
public class User extends EntityCommon {
    @Size(max = 120)
    @NotBlank
    private String username;
    @Size(max = 120)
    @NotBlank
    private String password;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;
    private String phone;
    private String fullName;
    private String statusSendMail;//Trang thai gui mail Y(gui thanh cong) N(gui that bai)

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roleInfo;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email, String phone, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
    }

    public static UserDto.UserSelectInfoResponse convertToDto(User request){
        UserDto.UserSelectInfoResponse objectDtoResponse = new UserDto.UserSelectInfoResponse();
        if(request != null){
            objectDtoResponse.setId(request.getId());
            objectDtoResponse.setUsername(request.getUsername());
            objectDtoResponse.setEmail(request.getEmail());
            objectDtoResponse.setPhone(request.getPhone());
            objectDtoResponse.setFullName(request.getFullName());
            objectDtoResponse.setCreateAt(request.getCreateAt());
            objectDtoResponse.setUpdateAt(request.getUpdateAt());
            objectDtoResponse.setRoles(request.getRoleInfo());
        }
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<UserDto.UserSelectListInfo> convertListObjectToDto(List<User> listRequestUser){
        PageAmtListResponseDto<UserDto.UserSelectListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<UserDto.UserSelectListInfo> listUserDto = new ArrayList<UserDto.UserSelectListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                UserDto.UserSelectListInfo newObject = new UserDto.UserSelectListInfo();
                newObject.setId(listRequestUser.get(i).getId());
                newObject.setUsername(listRequestUser.get(i).getUsername());
                newObject.setEmail(listRequestUser.get(i).getEmail());
                newObject.setPhone(listRequestUser.get(i).getPhone());
                newObject.setFullName(listRequestUser.get(i).getFullName());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setUpdateAt(listRequestUser.get(i).getUpdateAt());
                listUserDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listUserDto);
        objectDtoResponse.setTotalRecord(listRequestUser.size());
        return objectDtoResponse;
    }

    public static List<UserDto.UserSelectListInfo> convertListObjectStudentOrInstructorToDto(List<User> listRequestUser){
        List<UserDto.UserSelectListInfo> listUserDto = new ArrayList<UserDto.UserSelectListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                UserDto.UserSelectListInfo newObject = new UserDto.UserSelectListInfo();
                newObject.setId(listRequestUser.get(i).getId());
                newObject.setUsername(listRequestUser.get(i).getUsername());
                newObject.setEmail(listRequestUser.get(i).getEmail());
                newObject.setPhone(listRequestUser.get(i).getPhone());
                newObject.setFullName(listRequestUser.get(i).getFullName());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setUpdateAt(listRequestUser.get(i).getUpdateAt());
                listUserDto.add(newObject);
            }
        }
        return listUserDto;
    }



    public static PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> convertListStudentByInstructor(List<UserInfoDto> request){
        PageAmtListResponseDto<UserDto.AllStudentByInstructorInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<UserDto.AllStudentByInstructorInfo> listUserDto = new ArrayList<UserDto.AllStudentByInstructorInfo>();
        //Get information
        if(request.size() >0 ){
            for (int i=0;i<request.size();i++){
                UserDto.AllStudentByInstructorInfo newObject = new UserDto.AllStudentByInstructorInfo();
                newObject.setId(request.get(i).getId());
                newObject.setFullName(request.get(i).getFullName());
                listUserDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listUserDto);
        objectDtoResponse.setTotalRecord(request.size());
        return objectDtoResponse;
    }

    public static PageAmtListResponseDto<StudentDto.StudentSelectListInfo> convertListObjectToDtoStudent(List<User> listRequestUser){
        PageAmtListResponseDto<StudentDto.StudentSelectListInfo> objectDtoResponse = new PageAmtListResponseDto<>();
        List<StudentDto.StudentSelectListInfo> listUserDto = new ArrayList<StudentDto.StudentSelectListInfo>();
        if(listRequestUser.size() >0 ){
            for (int i=0;i<listRequestUser.size();i++){
                StudentDto.StudentSelectListInfo newObject = new StudentDto.StudentSelectListInfo();
                newObject.setId(listRequestUser.get(i).getId());
                newObject.setUsername(listRequestUser.get(i).getUsername());
                newObject.setEmail(listRequestUser.get(i).getEmail());
                newObject.setPhone(listRequestUser.get(i).getPhone());
                newObject.setFullName(listRequestUser.get(i).getFullName());
                newObject.setStatus(listRequestUser.get(i).getStatus());
                newObject.setCreateAt(listRequestUser.get(i).getCreateAt());
                newObject.setUpdateAt(listRequestUser.get(i).getUpdateAt());
                newObject.setRoles(listRequestUser.get(i).getRoleInfo());
                listUserDto.add(newObject);
            }
        }
        objectDtoResponse.setData(listUserDto);
        objectDtoResponse.setTotalRecord(listRequestUser.size());
        return objectDtoResponse;
    }
}
