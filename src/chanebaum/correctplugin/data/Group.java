package chanebaum.correctplugin.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Group {

	private int groupID;
	private ArrayList<String> groupMembers = new ArrayList<String>();

	public Group(int groupID) {
		super();
		this.groupID = groupID;
	}

	public int getGroupID() {
		return groupID;
	}

	public void addMember(String name) {
		groupMembers.add(name);
	}

	public ArrayList<String> getGroupMembers() {
		ArrayList<String> clone = new ArrayList<String>();

		for (String name : groupMembers) {
			clone.add(name);
		}

		return clone;
	}

	public boolean isMember(String name) {
		return groupMembers.contains(name);
	}

	/**
	 * Checks if the given contains homework that was turned in by one of the
	 * group members.
	 * 
	 * @param f
	 *            the homework folder to check for.
	 * @return true if the homework was turned in by one of the group members,
	 *         false if not.
	 */
	public boolean isHomework(File f) {
		for (String name : groupMembers) {
			if (f.getName().startsWith(name))
				return true;
		}
		return false;
	}

	public File findHomework(File dir) {
		ArrayList<File> studentdirs = new ArrayList<File>(Arrays.asList(dir
				.listFiles()));
		for (File f : studentdirs) {
			if (isHomework(f)) {
				if (f.listFiles().length == 1
						&& f.listFiles()[0].getName().endsWith(".zip")) {
					System.out.println("Hausaufgabe für Gruppe " + groupID + " gefunden: " + f.getName());
					return f.listFiles()[0];
				} else {
					throw new WrongFormatException("Directory " + f.getName()
							+ " does not contain a zip file!");
				}
			}
		}
		
		throw new NoHomeworkException("No Homework found for group " + groupID);
	}
	
	@Override
	public String toString() {
		return "Group [groupID=" + groupID + ", groupMembers=" + groupMembers
				+ "]";
	}

	public class WrongFormatException extends RuntimeException {

		public WrongFormatException(String string) {
			super(string);
		}

		private static final long serialVersionUID = -3660586943152580299L;
	}

	public class NoHomeworkException extends RuntimeException {

		public NoHomeworkException(String string) {
			super(string);
		}
		
		private static final long serialVersionUID = -3660586943152580299L;
	}

}
