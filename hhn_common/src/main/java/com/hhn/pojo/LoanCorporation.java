package com.hhn.pojo;

// Generated 2014-12-2 15:40:14 by Hibernate Tools 3.4.0.CR1

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * LoanCorporation generated by hbm2java
 */
@Alias("loan_corporation")
public class LoanCorporation implements java.io.Serializable {

	private Integer loan_corporation_id;
	private Integer user_id;
	private Integer loan_id;
	private Byte education;
	private Integer graduate_year;
	private String graduate_school;
	private Byte residence_type;
	private String family_phone;
	private Byte marriaged;
	private Byte has_children;
	private Byte has_house;
	private Byte has_house_loan;
	private Byte has_car;
	private Byte has_car_loan;
	private Integer household_address_id;
	private Integer family_address_id;
	private Integer residence_address_id;
	private Date create_time;
	private Date update_time;

	public LoanCorporation() {
	}

	public LoanCorporation(Integer loan_corporation_id, Integer user_id, Integer loan_id, Byte education, Integer graduate_year, String graduate_school, Byte residence_type, String family_phone, Byte marriaged, Byte has_children, Byte has_house, Byte has_house_loan, Byte has_car, Byte has_car_loan, Integer household_address_id, Integer family_address_id, Integer residence_address_id, Date create_time, Date update_time) {
		this.loan_corporation_id = loan_corporation_id;
		this.user_id = user_id;
		this.loan_id = loan_id;
		this.education = education;
		this.graduate_year = graduate_year;
		this.graduate_school = graduate_school;
		this.residence_type = residence_type;
		this.family_phone = family_phone;
		this.marriaged = marriaged;
		this.has_children = has_children;
		this.has_house = has_house;
		this.has_house_loan = has_house_loan;
		this.has_car = has_car;
		this.has_car_loan = has_car_loan;
		this.household_address_id = household_address_id;
		this.family_address_id = family_address_id;
		this.residence_address_id = residence_address_id;
		this.create_time = create_time;
		this.update_time = update_time;
	}

	public Integer getLoan_corporation_id() {
		return loan_corporation_id;
	}

	public void setLoan_corporation_id(Integer loan_corporation_id) {
		this.loan_corporation_id = loan_corporation_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getLoan_id() {
		return loan_id;
	}

	public void setLoan_id(Integer loan_id) {
		this.loan_id = loan_id;
	}

	public Byte getEducation() {
		return education;
	}

	public void setEducation(Byte education) {
		this.education = education;
	}

	public Integer getGraduate_year() {
		return graduate_year;
	}

	public void setGraduate_year(Integer graduate_year) {
		this.graduate_year = graduate_year;
	}

	public String getGraduate_school() {
		return graduate_school;
	}

	public void setGraduate_school(String graduate_school) {
		this.graduate_school = graduate_school;
	}

	public Byte getResidence_type() {
		return residence_type;
	}

	public void setResidence_type(Byte residence_type) {
		this.residence_type = residence_type;
	}

	public String getFamily_phone() {
		return family_phone;
	}

	public void setFamily_phone(String family_phone) {
		this.family_phone = family_phone;
	}

	public Byte getMarriaged() {
		return marriaged;
	}

	public void setMarriaged(Byte marriaged) {
		this.marriaged = marriaged;
	}

	public Byte getHas_children() {
		return has_children;
	}

	public void setHas_children(Byte has_children) {
		this.has_children = has_children;
	}

	public Byte getHas_house() {
		return has_house;
	}

	public void setHas_house(Byte has_house) {
		this.has_house = has_house;
	}

	public Byte getHas_house_loan() {
		return has_house_loan;
	}

	public void setHas_house_loan(Byte has_house_loan) {
		this.has_house_loan = has_house_loan;
	}

	public Byte getHas_car() {
		return has_car;
	}

	public void setHas_car(Byte has_car) {
		this.has_car = has_car;
	}

	public Byte getHas_car_loan() {
		return has_car_loan;
	}

	public void setHas_car_loan(Byte has_car_loan) {
		this.has_car_loan = has_car_loan;
	}

	public Integer getHousehold_address_id() {
		return household_address_id;
	}

	public void setHousehold_address_id(Integer household_address_id) {
		this.household_address_id = household_address_id;
	}

	public Integer getFamily_address_id() {
		return family_address_id;
	}

	public void setFamily_address_id(Integer family_address_id) {
		this.family_address_id = family_address_id;
	}

	public Integer getResidence_address_id() {
		return residence_address_id;
	}

	public void setResidence_address_id(Integer residence_address_id) {
		this.residence_address_id = residence_address_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
}