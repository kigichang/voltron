package xv.voltron.webapp.test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import xv.voltron.annotation.Field;
import xv.voltron.annotation.Table;
import xv.voltron.core.Model;

@Table
public class Admin extends Model {

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getDisabled() {
		return disabled;
	}

	public void setDisabled(Character disabled) {
		this.disabled = disabled;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	@Field(isPrimary=true, isAutoIncrement=true)
	private BigDecimal id;
	
	@Field
	private String account;
	
	@Field
	private String password;

	@Field
	private String name;
	
	@Field(defValue="N")
	private Character disabled;
	
	@Field(defValue="now")
	private Timestamp created;
	
	@Field(defValue="now")
	private Timestamp updated;
}
