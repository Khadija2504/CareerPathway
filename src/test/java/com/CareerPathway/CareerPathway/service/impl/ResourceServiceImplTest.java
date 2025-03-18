package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Resource;
import com.CareerPathway.CareerPathway.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private Resource resource;

    @BeforeEach
    void setUp() {
        resource = new Resource();
        resource.setId(1L);
        resource.setTitle("Test Resource");
    }

    @Test
    void findAll_ShouldReturnResources_WhenResourcesExist() {
        List<Resource> resources = Arrays.asList(resource);
        when(resourceRepository.findAll()).thenReturn(resources);

        List<Resource> result = resourceService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Resource", result.get(0).getTitle());
    }

    @Test
    void findAll_ShouldThrowException_WhenNoResourcesFound() {
        when(resourceRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () -> resourceService.findAll());
    }

    @Test
    void addResource_ShouldSaveAndReturnResource() {
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        Resource savedResource = resourceService.addResource(resource);

        assertNotNull(savedResource);
        assertEquals("Test Resource", savedResource.getTitle());
    }

    @Test
    void addResource_ShouldThrowException_WhenDataIntegrityViolationOccurs() {
        when(resourceRepository.save(any(Resource.class)))
                .thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        assertThrows(RuntimeException.class, () -> resourceService.addResource(resource));
    }

    @Test
    void deleteResource_ShouldReturnTrue_WhenResourceExists() {
        when(resourceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resourceRepository).deleteById(1L);

        boolean result = resourceService.deleteResource(1L);

        assertTrue(result);
        verify(resourceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteResource_ShouldThrowException_WhenResourceDoesNotExist() {
        when(resourceRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> resourceService.deleteResource(1L));
    }

    @Test
    void deleteResource_ShouldThrowException_WhenDeletionFails() {
        when(resourceRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Delete error")).when(resourceRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> resourceService.deleteResource(1L));
    }
}
