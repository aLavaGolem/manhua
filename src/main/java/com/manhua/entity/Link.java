package com.manhua.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "t_link" )
public class Link {
	private Long id;

	private String url;
	private String path;
	private Date createTime;
	private String name;
	private String parentName;
	private String parentUrl;
	private Long parentId;
	private Integer level;

	public Link (){

	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Link(String url, String path, Date createTime,Integer level) {
		this.url = url;
		this.path = path;
		this.createTime = createTime;
		this.level =level;
	}
	public Link(String url, String path, Date createTime,String name,String parentName,String parentUrl,Integer level) {
		this.url = url;
		this.path = path;
		this.createTime = createTime;
		this.name = name;
		this.parentName = parentName;
		this.parentUrl = parentUrl;
		this.level =level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name="parent_name")
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	@Column(name="parent_url")
	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	@Override
	public String toString() {
		return "Link [createTime=" + createTime + ", id=" + id + ", name=" + name + ", parentName=" + parentName
				+ ", parentUrl=" + parentUrl + ", path=" + path + ", url=" + url + "]";
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


}