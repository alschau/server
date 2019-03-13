package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class User implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long Id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true) 
	private String username;
	
	@Column(nullable = false, unique = true) 
	private String token;

	@Column(nullable = false)
	private UserStatus status;

	@Column(nullable = false)
	private String creationDate;

	// Birthday must be nullable = true because we don't get the information before we login
	@Column(nullable = true)
	private String birthday;

	@Column(nullable = false)
	private String password;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getBirthday() { return birthday; }

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}


	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(){
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - hh.mm");
		String nowStr = dateFormat.format(now);
		this.creationDate = nowStr;
	}


	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
}
