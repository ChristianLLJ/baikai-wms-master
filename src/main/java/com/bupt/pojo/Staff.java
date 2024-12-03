package com.bupt.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.bupt.DTO.IdAndSearchDTO;
import com.bupt.mapper.StaffDAO;
import com.bupt.service.authority.UserService;
import com.bupt.service.authority.WorkGroupService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author
 * 员工
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff extends BasePojo {
    /**
     * id
     */
    private Integer id;

    /**
     * 是否离职
     */
    private Byte isInPosition;

    /**
     * 姓名
     */
    @NotEmpty(message = "姓名不能为空")
    private String staffName;

    /**
     * 工号
     */
    private String staffId;

    /**
     * 所属公司名称
     */
    private String companyName;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 岗位id
     */
    private Integer positionId;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 上司id
     */
    private Integer leaderId;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 最近登陆时间
     */
    private Date latestTime;

    /**
     * 入职时间
     */
    private Date entryTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 身份证
     */
    private String identityCard;

    /**
     * 是否启用
     */
    private Byte isUsable;
    /**
     * 当前工作组
     */
    private String curWorkGroup;

    /**
     * 当前工作组id
     */
    private Integer curWorkGroupId;

    private static final long serialVersionUID = 1L;


}
