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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"username"}),
		@UniqueConstraint(columnNames = {"emailid"})
})
@NamedQueries({
	@NamedQuery(name="users.selectUserByUserName", query="SELECT instance from User instance where instance.username=:username" ),
	@NamedQuery(name="users.fetchAllUsers", query="SELECT instance from User instance" )
})
@XmlRootElement
public class User extends AbstractEntity implements IEntity, UserDetails, Serializable {

	private String username;
	private String password;

	private String fullName;
	private String emailId;

	private List<Project> projects;

	private boolean enabled = true;

	private Collection<GrantedAuthority> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;

	/* Getters and Setters */

	@Column(name = "username", nullable = false )
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false )
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "fullname")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "emailid" )
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

}
