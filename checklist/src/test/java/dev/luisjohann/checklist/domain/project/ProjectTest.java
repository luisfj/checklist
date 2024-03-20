package dev.luisjohann.checklist.domain.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dev.luisjohann.checklist.domain.project.exceptions.InvalidProjectNameException;

public class ProjectTest {

    @Test
    void testCreateProjectWithEmptySlug_thenRetrieveInvalidProjectNameException() {
        var thrown = assertThrows(InvalidProjectNameException.class, () -> {
            new Project(1L, "", "Name", "Description");
        }, "InvalidProjectNameException was expected");

        assertEquals("Project name invalid", thrown.getTitle());
        assertEquals("Project name must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateProjectWithNullSlug_thenRetrieveInvalidProjectNameException() {
        var thrown = assertThrows(InvalidProjectNameException.class, () -> {
            new Project(1L, null, "Name", "Description");
        }, "InvalidProjectNameException was expected");

        assertEquals("Project name invalid", thrown.getTitle());
        assertEquals("Project name must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateProjectWithNullName_thenRetrieveInvalidProjectNameException() {
        var thrown = assertThrows(InvalidProjectNameException.class, () -> {
            new Project(1L, "Slug", null, "Description");
        }, "InvalidProjectNameException was expected");

        assertEquals("Project name invalid", thrown.getTitle());
        assertEquals("Project name must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateProjectWithEmptyName_thenRetrieveInvalidProjectNameException() {
        var thrown = assertThrows(InvalidProjectNameException.class, () -> {
            new Project(1L, "Slug", "", "Description");
        }, "InvalidProjectNameException was expected");

        assertEquals("Project name invalid", thrown.getTitle());
        assertEquals("Project name must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateProjectWithIdAndDescriptionNull() {
        var project = new Project(null, "Slug", "Name", null);

        assertNull(project.id());
        assertNull(project.description());
        assertEquals("Slug", project.slug());
        assertEquals("Name", project.name());
    }

    @Test
    void testCreateProjectWithIdAndDescriptionEmpty() {
        var project = new Project(0L, "Slug", "Name", "");

        assertEquals(0L, project.id());
        assertEquals("", project.description());
        assertEquals("Slug", project.slug());
        assertEquals("Name", project.name());
    }
}
