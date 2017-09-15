package ru.insagent.workflow.model;

import java.util.ArrayList;
import java.util.List;

public class Item {
	private int id;
	private String name;
	private int cityId;
	private String cityName;
	private List<Integer> nodeIds = new ArrayList<Integer>();
	private int nodeTypeId;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public List<Integer> getNodeIds() {
		return nodeIds;
	}
	public void addNodeId(int nodeId) {
		if(!nodeIds.contains(nodeId)) {
			this.nodeIds.add(nodeId);
		}
	}
	public void setNodeIds(List<Integer> nodeIds) {
		this.nodeIds = nodeIds;
	}
	public int getNodeTypeId() {
		return nodeTypeId;
	}
	public void setNodeTypeId(int nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	public String toString() {
		//TODO Написать метод
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}
}
