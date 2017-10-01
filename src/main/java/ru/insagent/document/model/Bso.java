package ru.insagent.document.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import ru.insagent.model.IdBase;
import ru.insagent.model.Unit;
import ru.insagent.model.User;
import ru.insagent.workflow.model.Node;

public class Bso extends IdBase {
	private static final long serialVersionUID = -3893689810214482026L;

	private Date created;
	private String series;
	private long number;
	private boolean issued;
	private Date issuedDate;
	private User issuedBy;
	private Unit issuedUnit;
	private boolean corrupted;
	private Date corruptedDate;
	private User corruptedBy;
	private Unit corruptedUnit;
	private boolean registered;
	private Date registeredDate;
	private User registeredBy;
	private Unit registeredUnit;
	private String insured;
	private BigDecimal premium;
	private Unit unit;
	private Node node;

	@JSON(format = "dd.MM.yyyy HH:mm")
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public long getNumber() {
		return number;
	}
	public void setNumber(long l) {
		this.number = l;
	}
	public boolean isIssued() {
		return issued;
	}
	public void setIssued(boolean issued) {
		this.issued = issued;
	}
	@JSON(format = "dd.MM.yyyy HH:mm")
	public Date getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}
	public User getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(User user) {
		this.issuedBy = user;
	}
	public Unit getIssuedUnit() {
		return issuedUnit;
	}
	public void setIssuedUnit(Unit issuedUnit) {
		this.issuedUnit = issuedUnit;
	}
	public boolean isCorrupted() {
		return corrupted;
	}
	public void setCorrupted(boolean corrupted) {
		this.corrupted = corrupted;
	}
	@JSON(format = "dd.MM.yyyy HH:mm")
	public Date getCorruptedDate() {
		return corruptedDate;
	}
	public void setCorruptedDate(Date corruptedDate) {
		this.corruptedDate = corruptedDate;
	}
	public User getCorruptedBy() {
		return corruptedBy;
	}
	public void setCorruptedBy(User corruptedBy) {
		this.corruptedBy = corruptedBy;
	}
	public Unit getCorruptedUnit() {
		return corruptedUnit;
	}
	public void setCorruptedUnit(Unit corruptedUnit) {
		this.corruptedUnit = corruptedUnit;
	}
	public boolean isRegistered() {
		return registered;
	}
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	@JSON(format = "dd.MM.yyyy HH:mm")
	public Date getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	public User getRegisteredBy() {
		return registeredBy;
	}
	public void setRegisteredBy(User registeredBy) {
		this.registeredBy = registeredBy;
	}
	public Unit getRegisteredUnit() {
		return registeredUnit;
	}
	public void setRegisteredUnit(Unit registeredUnit) {
		this.registeredUnit = registeredUnit;
	}
	public String getInsured() {
		return insured;
	}
	public void setInsured(String insured) {
		this.insured = insured;
	}
	public BigDecimal getPremium() {
		return premium;
	}
	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "Bso [id=" + id + ", series=" + series + ", number=" + number + ", node=" + node + ", unit=" + unit + ", issued=" + issued + ", corrupted=" + corrupted + ", registered=" + registered + "]";
	}
}
