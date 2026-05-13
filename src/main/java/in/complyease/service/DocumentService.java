package in.complyease.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.dto.document.*;
import in.complyease.entity.*;
import in.complyease.repository.*;

@Service
public class DocumentService {

    @Autowired private DocumentRepository documentRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ComplianceRepository complianceRepository;
    @Autowired private NotificationService notificationService;
    @Autowired private EmailService emailService;

    //CREATE DOCUMENT
    @Transactional
    public DocumentResponse createDocument(DocumentRequest request, String email) {

        // 1. Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get business
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // Ownership check
        if (!business.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to this business");
        }

        // 3. Create document
        Document document = new Document();
        document.setBusiness(business);
        document.setDocumentFileName(request.getFileName());
        document.setDocumentFileUrl(request.getFileUrl());
        document.setDocumentType(request.getDocumentType());
        if (request.getComplianceId() != null) {
            Compliance compliance = complianceRepository.findById(request.getComplianceId())
                    .orElseThrow(() -> new RuntimeException("Compliance not found"));
            document.setCompliance(compliance);
        }

        // 4. Save
        Document saved = documentRepository.save(document);
        
        String message =
                "New document uploaded for business "
                + business.getBusinessName()
                + ". Document: "
                + saved.getDocumentFileName();
        
        notificationService.createNotification(
                business,
                message
        );
        
        if (business.getAssignedCA() != null) {

            emailService.sendEmail(
                    business.getAssignedCA().getEmail(),
                    "New Document Uploaded",
                    message
            );
        }

        // 5. Return response
        return new DocumentResponse(
                saved.getDocumentId(),
                business.getBusinessId(),
                saved.getCompliance() != null ? saved.getCompliance().getComplianceId() : null,
                business.getBusinessName(),
                saved.getDocumentFileName(),
                saved.getDocumentFileUrl(),
                saved.getDocumentType()
        );
    }

    //GET ALL DOCUMENTS (for logged-in user)
    public List<DocumentResponse> getUserDocuments(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Business> businesses = user.getBusinesses();

        List<Document> documents = documentRepository.findByBusinessIn(businesses);

        return documents.stream()
                .map(doc -> new DocumentResponse(
                        doc.getDocumentId(),
                        doc.getBusiness().getBusinessId(),
                     // Replace all instances of doc.getCompliance().getComplianceId() with:
                        doc.getCompliance() != null ? doc.getCompliance().getComplianceId() : null,
                        doc.getBusiness().getBusinessName(),
                        doc.getDocumentFileName(),
                        doc.getDocumentFileUrl(),
                        doc.getDocumentType()
                ))
                .collect(Collectors.toList());
    }

    //GET DOCUMENT BY ID
    public DocumentResponse getDocumentById(int documentId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        //Ownership check
        if (!document.getBusiness().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        return new DocumentResponse(
                document.getDocumentId(),
                document.getBusiness().getBusinessId(),
             // Replace all instances of doc.getCompliance().getComplianceId() with:
                document.getCompliance() != null ? document.getCompliance().getComplianceId() : null,
                document.getBusiness().getBusinessName(),
                document.getDocumentFileName(),
                document.getDocumentFileUrl(),
                document.getDocumentType()
        );
    }

    //DELETE DOCUMENT (soft delete handled by Hibernate)
    @Transactional
    public void deleteDocument(int documentId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Ownership check
        if (!document.getBusiness().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        documentRepository.delete(document);
    }
    
    public List<DocumentResponse> getAssignedBusinessDocuments(String email) {
        User ca = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("CA not found"));
        // ASSIGNED BUSINESSES
        List<Business> businesses = businessRepository.findByAssignedCA(ca);
        // DOCUMENTS
        List<Document> documents = documentRepository.findByBusinessIn(businesses);

        return documents.stream()
                .map(doc -> new DocumentResponse(
                        doc.getDocumentId(),
                        doc.getBusiness().getBusinessId(),
                     // Replace all instances of doc.getCompliance().getComplianceId() with:
                        doc.getCompliance() != null ? doc.getCompliance().getComplianceId() : null,
                        doc.getBusiness().getBusinessName(),
                        doc.getDocumentFileName(),
                        doc.getDocumentFileUrl(),
                        doc.getDocumentType()
                ))
                .toList();
    }
}