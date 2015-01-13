package com.hhn.pojo;

// Generated 2014-12-2 15:40:14 by Hibernate Tools 3.4.0.CR1

import org.apache.ibatis.type.Alias;

/**
 * SysIpAddress generated by hbm2java
 */
@Alias("sys_ip_address")
public class SysIpAddress implements java.io.Serializable {

	private Integer ip_address_id;
	private String ip;
	private String country;
	private String country_id;
	private String area;
	private Integer area_id;
	private String region;
	private Integer region_id;
	private String city;
	private Integer city_id;
	private String county;
	private Integer county_id;
	private String isp;
	private Integer isp_id;

	public SysIpAddress() {
	}

	public SysIpAddress(Integer ip_address_id, String ip, String country, String country_id, String area, Integer area_id, String region, Integer region_id, String city, Integer city_id, String county, Integer county_id, String isp, Integer isp_id) {
		this.ip_address_id = ip_address_id;
		this.ip = ip;
		this.country = country;
		this.country_id = country_id;
		this.area = area;
		this.area_id = area_id;
		this.region = region;
		this.region_id = region_id;
		this.city = city;
		this.city_id = city_id;
		this.county = county;
		this.county_id = county_id;
		this.isp = isp;
		this.isp_id = isp_id;
	}

	public Integer getIp_address_id() {
		return ip_address_id;
	}

	public void setIp_address_id(Integer ip_address_id) {
		this.ip_address_id = ip_address_id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getArea_id() {
		return area_id;
	}

	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Integer getCounty_id() {
		return county_id;
	}

	public void setCounty_id(Integer county_id) {
		this.county_id = county_id;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public Integer getIsp_id() {
		return isp_id;
	}

	public void setIsp_id(Integer isp_id) {
		this.isp_id = isp_id;
	}
}