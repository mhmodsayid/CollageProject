package entity;

import entity.Reader.Operation;
import entity.Reader.StatusMemberShip;

public class Reader {
	public enum StatusMemberShip{
		NotRegistered,Active,Frozen,Locked
	}
	public enum Operation{
		LendingReqest,ReturnBookRequest,ExtendBookRequest
		}
	private String student_id;
	private String student_name;
	private StatusMemberShip status;
	private Operation operation;
	private int freeze;
	
	
	
	
	public Reader() {
		super();
	}
	public Reader(String student_id, String student_name, int freeze, StatusMemberShip status, Operation operation) {
		super();
		this.student_id = student_id;
		this.student_name = student_name;
		this.freeze = freeze;
		this.status = status;
		this.operation = operation;
	}
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public int getFreeze() {
		return freeze;
	}
	public void setFreeze(int freeze) {
		this.freeze = freeze;
	}
	public StatusMemberShip getStatus() {
		return status;
	}
	public void setStatus(StatusMemberShip status) {
		this.status = status;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reader other = (Reader) obj;
		if (freeze != other.freeze)
			return false;
		if (operation != other.operation)
			return false;
		if (status != other.status)
			return false;
		if (student_id == null) {
			if (other.student_id != null)
				return false;
		} else if (!student_id.equals(other.student_id))
			return false;
		if (student_name == null) {
			if (other.student_name != null)
				return false;
		} else if (!student_name.equals(other.student_name))
			return false;
		return true;
	}
	public String toStringClient() {
		return student_id + "," + student_name + ","+ status 
				+ "," + operation + "," + freeze;
	}
	@Override
	public String toString() {
		return "Student [student_id=" + student_id + ", student_name=" + student_name + ", status=" + status
				+ ", operation=" + operation + ", freeze=" + freeze + "]";
	}
	
	
	
	
}
