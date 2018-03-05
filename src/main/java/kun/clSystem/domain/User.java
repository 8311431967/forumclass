package kun.clSystem.domain;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
@DynamicInsert
public class User implements Serializable {



	private Integer id;
	private String email;
	private String userName;
	private String password;
	private Integer sex;
	private Integer integral;

	private Integer numOfQuestion;
	private Integer numOfAnswer;

	@ManyToMany
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	//    private String checkCode;
//    @Transient
//    public String getCheckCode() {
//        return checkCode;
//    }
//
//    public void setCheckCode(String checkCode) {
//        this.checkCode = checkCode;
//    }

	@Transient
	public Integer getNumOfAnswer() {
		return numOfAnswer;
	}

	public void setNumOfAnswer(Integer numOfAnswer) {
		this.numOfAnswer = numOfAnswer;
	}
	@Transient
	public Integer getNumOfQuestion() {
		return numOfQuestion;
	}

	public void setNumOfQuestion(Integer numOfQuestion) {
		this.numOfQuestion = numOfQuestion;
	}

	public User(String email, String userName, String password) {
		this.email = email;
		this.userName = userName;
		this.password = password;
	}

	public User() {
	}

	public User(String email,String password){
		this.email=email;
		this.password=password;
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "username")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//    @Column(nullable = false,columnDefinition = "int(1) default 1")
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", sex=" + sex +
				", integral=" + integral +
				'}';
	}
}
