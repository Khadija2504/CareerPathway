package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.service.CourseService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public List<Course> fetchCoursesFromThirdPartyPlatforms() {
        List<Course> courses = new ArrayList<>();

        courses.addAll(fetchUdemyCourses());

        courses.addAll(fetchCourseraCourses());

        courses.addAll(fetchLinkedInCourses());

        return courses;
    }
    @Override
    public List<Course> fetchUdemyCourses() {
        List<Course> courses = new ArrayList<>();
        String apiUrl = "https://api.udemy.com/v1/courses";

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer YOUR_UDEMY_ACCESS_TOKEN")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // Parse JSON response and map to Course objects
                // Example: courses = objectMapper.readValue(response.body().string(), new TypeReference<List<Course>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }
    @Override
    public List<Course> fetchCourseraCourses() {
        List<Course> courses = new ArrayList<>();
        String apiUrl = "https://api.coursera.org/v1/courses";

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer YOUR_COURSERA_ACCESS_TOKEN")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // Parse JSON response and map to Course objects
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }

    @Override
    public List<Course> fetchLinkedInCourses() {
        List<Course> courses = new ArrayList<>();
        String apiUrl = "https://api.linkedin.com/v2/learningAssets";

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer YOUR_LINKEDIN_ACCESS_TOKEN")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // Parse JSON response and map to Course objects
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }
}
