package ru.insagent.document.model;

import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import ru.insagent.management.model.Unit;
import ru.insagent.model.IdBase;
import ru.insagent.workflow.model.Node;

public class Act extends IdBase {
	private static final long serialVersionUID = 6963271182212532121L;

	private ActType type;
	private Date created;
	private Node NodeFrom;
	private Node NodeTo;
	private Unit UnitFrom;
	private Unit UnitTo;
	private int amount = 0;
	private String comment = "";
	private List<ActPack> packs;

	public ActType getType() {
		return type;
	}
	public void setType(ActType type) {
		this.type = type;
	}
	@JSON(format = "dd.MM.yyyy HH:mm")
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Node getNodeFrom() {
		return NodeFrom;
	}
	public void setNodeFrom(Node nodeFrom) {
		NodeFrom = nodeFrom;
	}
	public Node getNodeTo() {
		return NodeTo;
	}
	public void setNodeTo(Node nodeTo) {
		NodeTo = nodeTo;
	}
	public Unit getUnitFrom() {
		return UnitFrom;
	}
	public void setUnitFrom(Unit unitFrom) {
		UnitFrom = unitFrom;
	}
	public Unit getUnitTo() {
		return UnitTo;
	}
	public void setUnitTo(Unit unitTo) {
		UnitTo = unitTo;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<ActPack> getPacks() {
		return packs;
	}
	public void setPacks(List<ActPack> packs) {
		this.packs = packs;
	}

	@Override
	public String toString() {
		return "Act [id=" + id + ", type=" + type + ", created=" + created + ", NodeFrom=" + NodeFrom + ", NodeTo=" + NodeTo + ", UnitFrom=" + UnitFrom + ", UnitTo=" + UnitTo + ", comment=" + comment	+ ", packs=" + packs + "]";
	}
}
