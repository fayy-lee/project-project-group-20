package com.example.hams;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTests {
    private Doctor doctorTest;
    private Patient patientTest;
    private Admin adminTest;
    private MainActivity main;
    private UpcomingAppointments upComingA;
    private User userToApprove;
    private User userToReject;

    @Before
    public void init() {
        doctorTest = new Doctor();
        patientTest = new Patient();
        adminTest = new Admin();
        main = new MainActivity();
        upComingA = new UpcomingAppointments();

        doctorTest.setFirstName("John");
        doctorTest.setLastName("Doe");
        doctorTest.setPhoneNumber("1234567890");
        doctorTest.setAddress("123 Main St");
        doctorTest.setEmployeeNumber("EMP123");
        doctorTest.setSpecialties("Cardiology");

        adminTest = new Admin();
        userToApprove = new User("UserToApprove", "Password123");
        userToReject = new User("UserToReject", "Password456");
    }

    @Test
    public void testAutoApproveAppointments() {
        assertFalse(doctorTest.getAutoApproveAppointments()); // By default, it should be false

        doctorTest.setAutoApproveAppointments(true);
        assertTrue(doctorTest.getAutoApproveAppointments());
    }

    @Test
    public void testUpcomingAppointments() {
        assertTrue(doctorTest.getUpcomingAppointments().isEmpty()); // Initially, it should be empty

        Appointment upcomingAppointment = new Appointment();
        doctorTest.addUpcomingAppointment(upcomingAppointment);

        assertEquals(1, doctorTest.getUpcomingAppointments().size());
        assertTrue(doctorTest.getUpcomingAppointments().contains(upcomingAppointment));
    }

    @Test
    public void testSetAndGetFirstName() {
        patientTest.setFirstName("John");
        assertEquals("John", patientTest.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        patientTest.setLastName("Doe");
        assertEquals("Doe", patientTest.getLastName());
    }

    @Test
    public void testSetAndGetAddress() {
        patientTest.setAddress("123 Main St");
        assertEquals("123 Main St", patientTest.getAddress());
    }

    @Test
    public void testInitialization() {
        assertEquals("Admin20", adminTest.getUser());
        assertEquals("AdminPass123", adminTest.getPass());
        assertEquals("Approved", adminTest.getStatus());
    }

    @Test
    public void testApprove() {
        adminTest.approve(userToApprove);
        assertEquals("Approved", userToApprove.getStatus());
    }

    @Test
    public void testReject() {
        adminTest.reject(userToReject);
        assertEquals("Rejected", userToReject.getStatus());
    }
}
