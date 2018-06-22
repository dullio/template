
package br.com.va4e.gidac.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"street", "number", "complement", "cep", "city", "state", "country", "type", "member_id"}), name = "member_address")
public class MemberAddress implements Serializable {

	public MemberAddress() {

	}

	public MemberAddress(String street, String number, String complement, String cep, String city, String state,
			String country, int type, Member member) {

		this.street = street;
		this.number = number;
		this.complement = complement;
		this.cep = cep;
		this.city = city;
		this.state = state;
		this.country = country;
		this.type = type;
		this.member = member;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String street;

	@NotNull
	private String number;

	@NotNull
	private String complement;

	private String cep;

	private String city;

	private String state;

	private String country;

	private int type;

	@NotNull
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "member_id") // @JsonManagedReference
	private Member member;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "MemberAddress [id=" + id + ", street=" + street + ", number=" + number + ", complement=" + complement
				+ ", cep=" + cep + ", city=" + city + ", state=" + state + ", country=" + country + ", type=" + type
				+ "]";
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
