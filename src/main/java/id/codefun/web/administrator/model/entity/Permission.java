package id.codefun.web.administrator.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import id.codefun.common.model.entity.BaseEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "permissions", indexes = {
	@Index(name = "PERMISSIONS_INDX_1", columnList = "permission_level"),
	@Index(name = "PERMISSIONS_INDX_2", columnList = "parent_id"),
	@Index(name = "PERMISSIONS_INDX_3", columnList = "permission_type")
})
public class Permission extends BaseEntity {

	@Id
	private Long id;

	@Column(name = "permission_name", length = 100)
	private String permissionName;

	@Column(name = "permission_level")
	private Integer permissionLevel;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "title", length = 100)
	private String title;

	@Column(name = "icon", length = 150)
	private String icon;

	@Column(name = "api")
	private String api;

	@Column(name = "permission_path", length = 100)
	private String permissionPath;

	@Column(name = "permission_type")
	private Integer permissionType;

}
