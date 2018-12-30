package controller;

import java.sql.SQLException;
import java.util.List;

import entity.Reader;

public class CreateObject {
	public static void createObject(Object type,List<String> data) throws SQLException {//mapping from data to the object
		  if(type instanceof Reader) {
			Reader student=(Reader) type;
			student.setStudent_id(data.get(0));
			student.setStudent_name(data.get(1));
			student.setStatus(Reader.StatusMemberShip.valueOf(data.get(2)));
			student.setOperation(Reader.Operation.valueOf(data.get(3)));
			student.setFreeze(Integer.parseInt(data.get(4)));
		  }	  

		  }
}
