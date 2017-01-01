package com.senomas.webhookbot.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MESSAGE", indexes = { @Index(unique = false, columnList = "HOOK_TYPE, HOOK") })
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@Column(name = "HOOK_TYPE", length = 100)
	private String hookType;

	@NotNull
	@Column(name = "HOOK", length = 100)
	private String hook;

	@NotNull
	@Lob
	@Column(name = "DATA")
	private String data;

	@NotNull
	@Column(name = "TIMESTAMP")
	private Date timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHookType() {
		return hookType;
	}

	public void setHookType(String hookType) {
		this.hookType = hookType;
	}

	public String getHook() {
		return hook;
	}

	public void setHook(String hook) {
		this.hook = hook;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((hook == null) ? 0 : hook.hashCode());
		result = prime * result + ((hookType == null) ? 0 : hookType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (hook == null) {
			if (other.hook != null)
				return false;
		} else if (!hook.equals(other.hook))
			return false;
		if (hookType == null) {
			if (other.hookType != null)
				return false;
		} else if (!hookType.equals(other.hookType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Message [id=%s, hookType=%s, hook=%s, data=%s, timestamp=%s]", id, hookType, hook, data,
				timestamp);
	}
}
