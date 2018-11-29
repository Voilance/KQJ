package com.biketomotor.kqj.Class;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseList {
    private static final String TAG = "TagCourseList";

    public static class Course {
        public String courseName;
        public String classroom;
        public String teacher;
        public ArrayList<Integer> weeks;
        public int weekday;
        public int startLesson;
        public int endLesson;

        public Course(JSONObject data) {
            try {
                courseName = data.getString("courseName");
                classroom = data.getString("classroom");
                teacher = data.getString("teacher");
                weekday = data.getInt("weekDay");
                startLesson = data.getInt("startTime");
                endLesson = data.getInt("endTime");

                JSONArray weeksArray = data.getJSONArray("weeks");
                weeks = new ArrayList<>();
                for (int i = 0; i < weeksArray.length(); i++) {
                    weeks.add((int)weeksArray.get(i));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Course:" + e.toString());
            }
        }

        public String info() {
            return "@" + courseName + "@" + classroom;
        }

        @Override
        public String toString() {
            return "courseName:" + courseName +
                    " classroom:" + classroom +
                    " teacher:" + teacher +
                    " startLesson:" + String.valueOf(startLesson) +
                    " endLesson:" + String.valueOf(endLesson) +
                    "weekday:" + String.valueOf(weekday) +
                    "weeks:" + weeks.toString();
        }
    }

    public static int numOfCourse = 0;
    public static HashMap<Integer, Course> courseMap = new HashMap<>(0);

    public static void initCourseList(JSONObject data) {
        courseMap.clear();
        try {
            JSONArray courseList = new JSONArray(data.getString("courseList"));
            numOfCourse = courseList.length();
            for (int i = 0; i < numOfCourse; i++) {
                JSONObject course = courseList.getJSONObject(i);
                int courseID = course.getInt("courseId");
                JSONObject courseData = course.getJSONObject("courseData");
                courseMap.put(courseID, new Course(courseData));
            }
        } catch (JSONException e) {
            Log.e(TAG, "initCourseList:" + e.toString());
        }
    }
}
