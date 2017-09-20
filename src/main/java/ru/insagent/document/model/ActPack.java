package ru.insagent.document.model;

import ru.insagent.model.IdBase;

public class ActPack extends IdBase {
	private static final long serialVersionUID = 3681691887322137171L;

	private String series;
	private long numberFrom;
	private long numberTo;
	private int amount;


	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public long getNumberFrom() {
		return numberFrom;
	}
	public void setNumberFrom(long numberFrom) {
		this.numberFrom = numberFrom;
	}
	public long getNumberTo() {
		return numberTo;
	}
	public void setNumberTo(long numberTo) {
		this.numberTo = numberTo;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "ActPack [id=" + id + ", series=" + series + ", numberFrom=" + numberFrom + ", numberTo=" + numberTo + ", amount=" + amount + "]";
	}
}
