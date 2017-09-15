package ru.insagent.document.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ru.insagent.management.model.Unit;
import ru.insagent.model.Filter;
import ru.insagent.workflow.model.Node;

public class BsoFilter extends Filter {
	private static final long serialVersionUID = -3893689810214482026L;

	private String series;
	private Long numberFrom;
	private Long numberTo;
	private String insured;
	private BigDecimal premiumFrom;
	private BigDecimal premiumTo;
	private List<Node> nodes;
	private List<Unit> units;
	private Date createdFrom;
	private Date createdTo;
	private Date issuedFrom;
	private Date issuedTo;
	private Date corruptedFrom;
	private Date corruptedTo;
	private Date registeredFrom;
	private Date registeredTo;

	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public Long getNumberFrom() {
		return numberFrom;
	}
	public void setNumberFrom(Long numberFrom) {
		this.numberFrom = numberFrom;
	}
	public Long getNumberTo() {
		return numberTo;
	}
	public void setNumberTo(Long numberTo) {
		this.numberTo = numberTo;
	}
	public String getInsured() {
		return insured;
	}
	public void setInsured(String insured) {
		this.insured = insured;
	}
	public BigDecimal getPremiumFrom() {
		return premiumFrom;
	}
	public void setPremiumFrom(BigDecimal premiumFrom) {
		this.premiumFrom = premiumFrom;
	}
	public BigDecimal getPremiumTo() {
		return premiumTo;
	}
	public void setPremiumTo(BigDecimal premiumTo) {
		this.premiumTo = premiumTo;
	}
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	public Date getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(Date createdFrom) {
		this.createdFrom = createdFrom;
	}
	public Date getCreatedTo() {
		return createdTo;
	}
	public void setCreatedTo(Date createdTo) {
		this.createdTo = createdTo;
	}
	public Date getIssuedFrom() {
		return issuedFrom;
	}
	public void setIssuedFrom(Date issuedFrom) {
		this.issuedFrom = issuedFrom;
	}
	public Date getIssuedTo() {
		return issuedTo;
	}
	public void setIssuedTo(Date issuedTo) {
		this.issuedTo = issuedTo;
	}
	public Date getCorruptedFrom() {
		return corruptedFrom;
	}
	public void setCorruptedFrom(Date corruptedFrom) {
		this.corruptedFrom = corruptedFrom;
	}
	public Date getCorruptedTo() {
		return corruptedTo;
	}
	public void setCorruptedTo(Date corruptedTo) {
		this.corruptedTo = corruptedTo;
	}
	public Date getRegisteredFrom() {
		return registeredFrom;
	}
	public void setRegisteredFrom(Date registeredFrom) {
		this.registeredFrom = registeredFrom;
	}
	public Date getRegisteredTo() {
		return registeredTo;
	}
	public void setRegisteredTo(Date registeredTo) {
		this.registeredTo = registeredTo;
	}

	@Override
	public String toString() {
		return "BsoFilter [series=" + series + ", numberFrom=" + numberFrom + ", numberTo=" + numberTo + ", insured=" + insured + ", premiumFrom=" + premiumFrom + ", premiumTo=" + premiumTo + ", nodes=" + nodes + ", units=" + units
				+ ", createdFrom=" + createdFrom + ", createdTo=" + createdTo
				+ ", issuedFrom=" + issuedFrom + ", issuedTo=" + issuedTo
				+ ", corruptedFrom=" + corruptedFrom + ", corruptedTo=" + corruptedTo
				+ ", registeredFrom=" + registeredFrom + ", registeredTo=" + registeredTo
				+ "]";
	}
}
