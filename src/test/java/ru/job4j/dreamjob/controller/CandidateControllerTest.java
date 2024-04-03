package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CandidateControllerTest {

    private CandidateService candidateService;

    private CityService cityService;

    private CandidateController candidateController;

    private MultipartFile testFile;

    private HttpServletRequest request;

    private Model model;

    @BeforeEach
    public void initServices() {
        candidateService = mock(CandidateService.class);
        cityService = mock(CityService.class);
        candidateController = new CandidateController(candidateService, cityService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
        request = mock(HttpServletRequest.class);
        model = new ConcurrentModel();
    }

    @Test
    void whenGetAllIsOK() {
        var candidate1 = new Candidate(1, "FirstName", "FirstDesc", LocalDateTime.now(), 1, 1);
        var candidate2 = new Candidate(2, "SecondName", "SecondDesc", LocalDateTime.now(), 2, 2);
        var candidate3 = new Candidate(3, "ThirdName", "ThirdDesc", LocalDateTime.now(), 3, 3);
        var candidatesList = List.of(candidate1, candidate2, candidate3);
        when(candidateService.findAll()).thenReturn(candidatesList);
        when(request.getSession()).thenReturn(new MockHttpSession());
        assertThat(candidateController.getAll(model, request)).isEqualTo("candidates/list");
    }

    @Test
    void whenGetCreationPageIsOK() {
        assertThat(candidateController.getCreationPage(model)).isEqualTo("candidates/create");
    }

    @Test
    void whenCreateIsOk() {
        var candidate1 = new Candidate(1, "FirstName", "FirstDesc", LocalDateTime.now(), 1, 1);
        assertThat(candidateController.create(candidate1, testFile, model)).isEqualTo("redirect:/candidates");
    }

    @Test
    void whenCreateReturnsError() {
        var candidate1 = new Candidate(1, "FirstName", "FirstDesc", LocalDateTime.now(), 1, 1);
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.save(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenThrow(new RuntimeException());
        assertThat(candidateController.create(candidate1, testFile, model)).isEqualTo("errors/404");
    }

    @Test
    void getByIdReturnsPage() {
        var candidate1 = new Candidate(1, "FirstName", "FirstDesc", LocalDateTime.now(), 1, 1);
        int id = candidate1.getId();
        when(candidateService.findById(id)).thenReturn(Optional.of(candidate1));
        assertThat(candidateController.getById(model, id)).isEqualTo("candidates/one");
    }

    @Test
    void getByIdReturnsError() {
        var candidate1 = new Candidate(1, "FirstName", "FirstDesc", LocalDateTime.now(), 1, 1);
        int id = candidate1.getId();
        assertThat(candidateController.getById(model, id)).isEqualTo("errors/404");
    }

    @Test
    void whenUpdateReturnsPage() {
        var candidate1 = new Candidate(1, "FirstName", "FirstDesc", LocalDateTime.now(), 1, 1);
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.update(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(true);
        assertThat(candidateController.update(candidate1, testFile, model)).isEqualTo("redirect:/candidates");
    }

    @Test
    void whenUpdateReturnsError() {
        var candidate1 = new Candidate(1, "FirstName", "FirstDesc", LocalDateTime.now(), 1, 1);
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.update(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(false);
        assertThat(candidateController.update(candidate1, testFile, model)).isEqualTo("errors/404");
    }

    @Test
    void delete() {
        var candidate1 = new Candidate(1, "FirstName", "FirstDesc", LocalDateTime.now(), 1, 1);
        int id = candidate1.getId();
        when(candidateService.deleteById(id)).thenReturn(true);
        assertThat(candidateController.delete(model, id)).isEqualTo("redirect:/candidates");
    }
}