package com.fasten.wp4.iot.kafka.request;

import java.io.Serializable;
import java.util.Date;

public class HistoricalDemandRequest implements Serializable {

	private Date initialDate;// = new GregorianCalendar(2015, 06, 26).getTime();
	private Date endDate;// = new GregorianCalendar(2017, 06, 28).getTime();

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
