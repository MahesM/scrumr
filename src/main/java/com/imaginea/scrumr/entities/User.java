package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"userid"}),
})
@NamedQueries({
	@NamedQuery(name="users.selectUserByUserName", query="SELECT instance from User instance where instance.username=:username" ),
	@NamedQuery(name="users.fetchAllUsers", query="SELECT instance from User instance" )
})
@XmlRootElement
public class User extends AbstractEntity implements IEntity, UserDetails, Serializable {

	private String username;
	private String displayname;
	private String fullname;
	private String emailid;
	private String avatarurl;
	private String password;

	private List<Project> projects;

	private boolean enabled = true;

	private Collection<GrantedAuthority> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;

	/* Getters and Setters */

	@Column(name = "userid", nullable = false )
	public String getUsername() {
		return username;
	}

	public void setUsername(String userid) {
		this.username = userid;
	}

	@Column(name = "displayname", nullable = false )
	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	@Column(name = "fullname", nullable = false )
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	@Column(name = "emailid", nullable = false )
	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	

	@Column(name = "avatarurl", nullable = false )
	public String getAvatarurl() {
		return avatarurl;
	}

	public void setAvatarurl(String avatarurl) {
		this.avatarurl = avatarurl;
	}

	@JsonIgnore
	@ManyToMany(fetch= FetchType.LAZY, cascade = CascadeType.ALL, targetEntity=Project.class)
	@JoinTable(name = "project_users", joinColumns = { @JoinColumn(name = "puserid") }, inverseJoinColumns = { @JoinColumn(name = "ppid") })
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	@XmlTransient
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void addProject(Project project){
		this.projects.add(project);
	}

	public void removeProject(Project project){
		this.projects.add(project);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
