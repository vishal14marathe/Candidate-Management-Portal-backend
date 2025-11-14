package com.project.service;

import com.project.dto.CandidateDTO;
import com.project.model.Candidate;
import com.project.repository.CandidateRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<String> importCandidatesFromExcel(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        if (rows.hasNext()) {
            rows.next();
        }

        int rowNumber = 1;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            rowNumber++;

            try {
                String fullName = getCellValueAsString(currentRow.getCell(0));
                Integer age = getCellValueAsInteger(currentRow.getCell(1));
                String qualification = getCellValueAsString(currentRow.getCell(2));
                String identityProofNumber = getCellValueAsString(currentRow.getCell(3));
                String location = getCellValueAsString(currentRow.getCell(4));
                String email = getCellValueAsString(currentRow.getCell(5));
                String mobileNumber = getCellValueAsString(currentRow.getCell(6));
                String occupationStatus = getCellValueAsString(currentRow.getCell(7));

                if (email == null || email.isEmpty()) {
                    errors.add("Row " + rowNumber + ": Email is required");
                    continue;
                }

                if (candidateRepository.existsByEmail(email)) {
                    errors.add("Row " + rowNumber + ": Email already exists - " + email);
                    continue;
                }

                if (candidateRepository.existsByIdentityProofNumber(identityProofNumber)) {
                    errors.add("Row " + rowNumber + ": Identity proof number already exists - " + identityProofNumber);
                    continue;
                }

                Candidate candidate = new Candidate();
                candidate.setFullName(fullName);
                candidate.setAge(age);
                candidate.setQualification(qualification);
                candidate.setIdentityProofNumber(identityProofNumber);
                candidate.setLocation(location);
                candidate.setEmail(email);
                candidate.setMobileNumber(mobileNumber);
                candidate.setOccupationStatus(occupationStatus);

                candidateRepository.save(candidate);
            } catch (Exception e) {
                errors.add("Row " + rowNumber + ": " + e.getMessage());
            }
        }

        workbook.close();
        return errors;
    }

    public byte[] exportCandidatesToExcel(String qualification, String occupationStatus, String location)
            throws IOException {
        List<CandidateDTO> candidates = getCandidatesWithFilters(qualification, occupationStatus, location);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Candidates");

        Row headerRow = sheet.createRow(0);
        String[] headers = { "ID", "Full Name", "Age", "Qualification", "Identity Proof Number",
                "Location", "Email", "Mobile Number", "Occupation Status", "Created At" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (CandidateDTO candidate : candidates) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(candidate.getId());
            row.createCell(1).setCellValue(candidate.getFullName());
            row.createCell(2).setCellValue(candidate.getAge());
            row.createCell(3).setCellValue(candidate.getQualification());
            row.createCell(4).setCellValue(candidate.getIdentityProofNumber());
            row.createCell(5).setCellValue(candidate.getLocation());
            row.createCell(6).setCellValue(candidate.getEmail());
            row.createCell(7).setCellValue(candidate.getMobileNumber());
            row.createCell(8).setCellValue(candidate.getOccupationStatus());
            row.createCell(9).setCellValue(candidate.getCreatedAt() != null ? candidate.getCreatedAt().toString() : "");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private List<CandidateDTO> getCandidatesWithFilters(String qualification, String occupationStatus,
            String location) {
        List<Candidate> candidates = candidateRepository.findAll((root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (qualification != null && !qualification.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("qualification"), qualification));
            }
            if (occupationStatus != null && !occupationStatus.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("occupationStatus"), occupationStatus));
            }
            if (location != null && !location.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("location"), location));
            }

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        List<CandidateDTO> candidateDTOs = new ArrayList<>();
        for (Candidate candidate : candidates) {
            candidateDTOs.add(modelMapper.map(candidate, CandidateDTO.class));
        }
        return candidateDTOs;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null)
            return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null)
            return null;
        return switch (cell.getCellType()) {
            case NUMERIC -> (int) cell.getNumericCellValue();
            case STRING -> Integer.parseInt(cell.getStringCellValue());
            default -> null;
        };
    }
}
