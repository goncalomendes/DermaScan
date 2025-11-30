package com.dermascan.api.controller;

import com.dermascan.api.model.User;
import com.dermascan.api.repository.UserRepository;
import com.dermascan.api.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ScanControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtUtils jwtUtils;

        @Autowired
        private ObjectMapper objectMapper;

        private String jwtToken;
        private User testUser;

        @BeforeEach
        public void setUp() {
                // Create a test user
                testUser = new User();
                testUser.setUsername("scanuser");
                testUser.setEmail("scan@example.com");
                testUser.setPassword(passwordEncoder.encode("password123"));
                testUser = userRepository.save(testUser);

                UserDetails userDetails = testUser;

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, // Pass UserDetails instead of String
                                testUser.getPassword(),
                                Collections.singletonList(new SimpleGrantedAuthority("USER")));
                jwtToken = jwtUtils.generateJwtToken(authentication);
        }

        @Test
        public void testUploadScan_Success() throws Exception {
                MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "test-image.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test image content".getBytes());

                mockMvc.perform(multipart("/api/scans/upload")
                                .file(file)
                                .param("bodyLocation", "left-arm")
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", notNullValue()))
                                .andExpect(jsonPath("$.imagePath", containsString("test-image.jpg")))
                                .andExpect(jsonPath("$.bodyLocation", is("left-arm")))
                                .andExpect(jsonPath("$.analysisResult", is("Pending")));
        }

        @Test
        public void testUploadScan_WithoutAuthentication() throws Exception {
                MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "test-image.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test image content".getBytes());

                mockMvc.perform(multipart("/api/scans/upload")
                                .file(file)
                                .param("bodyLocation", "left-arm"))
                                .andExpect(status().isForbidden());
        }

        @Test
        public void testUploadScan_EmptyFile() throws Exception {
                MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "empty.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                new byte[0]);

                mockMvc.perform(multipart("/api/scans/upload")
                                .file(file)
                                .param("bodyLocation", "left-arm")
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(containsString("Please select a file")));
        }

        @Test
        public void testGetUserScans_Success() throws Exception {
                // First upload a scan
                MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "test-image.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test image content".getBytes());

                mockMvc.perform(multipart("/api/scans/upload")
                                .file(file)
                                .param("bodyLocation", "left-arm")
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isOk());

                // Now get the scans
                mockMvc.perform(get("/api/scans")
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                                .andExpect(jsonPath("$[0].bodyLocation", is("left-arm")));
        }

        @Test
        public void testGetUserScans_WithoutAuthentication() throws Exception {
                mockMvc.perform(get("/api/scans"))
                                .andExpect(status().isForbidden());
        }

        @Test
        public void testGetScanById_Success() throws Exception {
                // First upload a scan
                MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "test-image.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test image content".getBytes());

                String response = mockMvc.perform(multipart("/api/scans/upload")
                                .file(file)
                                .param("bodyLocation", "right-leg")
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                Long scanId = objectMapper.readTree(response).get("id").asLong();

                // Now get the specific scan
                mockMvc.perform(get("/api/scans/" + scanId)
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(scanId.intValue())))
                                .andExpect(jsonPath("$.bodyLocation", is("right-leg")));
        }

        @Test
        public void testGetScanById_NotFound() throws Exception {
                mockMvc.perform(get("/api/scans/99999")
                                .header("Authorization", "Bearer " + jwtToken))
                                .andExpect(status().isNotFound());
        }
}
