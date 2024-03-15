package degreesmart;

import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseList {
	private ArrayList<Course> courses;
	private HashMap<UUID, Course> coursesByUuid;
	private HashMap<String, Course> coursesByShortName;
	private static CourseList courseList;

	private CourseList() {
		courses = DataLoader.getCourses();
		coursesByUuid = new HashMap<UUID, Course>();
		coursesByShortName = new HashMap<String, Course>();

		for (Course course : courses) {
			coursesByUuid.put(course.getUuid(), course);
			coursesByShortName.put(getShortName(course), course);
		}
	}

	public static CourseList getInstance() {
		if (courseList == null) {
			courseList= new CourseList();
		}
		
		return courseList;
	}

	private UUID getNextUuid() {
		UUID uuid;
		do {
			uuid = UUID.randomUUID();
		} while (coursesByUuid.containsKey(uuid));
		return uuid;
	}

	private String getShortName(Subject subject, String number) {
		return subject + " " + number;
	}

	private String getShortName(Course course) {
		return getShortName(course.getSubject(), course.getNumber());
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public Course getCourse(UUID uuid) {
		return coursesByUuid.get(uuid);
	}

	public Course getCourse(Subject subject, String number) {
		return coursesByShortName.get(getShortName(subject, number));
	}

	public Course createCourse(Subject subject, String number) {
		if (getCourse(subject, number) != null) {
			return null;
		} else {
			Course course = new Course(getNextUuid(), subject, number);
			coursesByUuid.put(course.getUuid(), course);
			coursesByShortName.put(getShortName(course), course);
			return course;
		}
	}

	public boolean deleteCourse(Course course) {
		if (courses.remove(course)) {
			coursesByUuid.remove(course.getUuid());
			coursesByShortName.remove(getShortName(course));
			return true;
		} else {
			return false;
		}
	}

	public boolean modifyCourse(Course course) {
		Course original = coursesByUuid.get(course.getUuid());

		if (original == null) {
			return false;
		} else {
			coursesByUuid.put(course.getUuid(), course);
			coursesByShortName.remove(getShortName(original));
			coursesByShortName.put(getShortName(course), course);
			return true;
		}
	}
}
