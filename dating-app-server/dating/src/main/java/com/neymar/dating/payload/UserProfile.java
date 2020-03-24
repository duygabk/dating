package com.neymar.dating.payload;

import java.time.Instant;

public class UserProfile {
	private Long id;
	private String username;
	private String name;
	private String avatar;
	private String country;
	private String province;
	private String description;
	private Instant createdAt;

	public UserProfile(final Long id, final String username, final String name, final String avatar,
			final String country, final String province, final String description, final Instant createdAt) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.avatar = avatar;
		this.country = country;
		this.province = province;
		this.description = description;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
