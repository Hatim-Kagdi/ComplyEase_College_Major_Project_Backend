package in.complyease.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.complyease.dto.document.DocumentRequest;
import in.complyease.dto.document.DocumentResponse;
import in.complyease.service.DocumentService;

@RestController
@RequestMapping("/user/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    //CREATE
    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(
            @RequestBody DocumentRequest request,
            Principal principal) {

        String email = principal.getName();

        return ResponseEntity.ok(
                documentService.createDocument(request, email)
        );
    }

    //GET ALL
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments(
            Principal principal) {

        String email = principal.getName();

        return ResponseEntity.ok(
                documentService.getUserDocuments(email)
        );
    }

    //GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(
            @PathVariable int id,
            Principal principal) {

        String email = principal.getName();

        return ResponseEntity.ok(
                documentService.getDocumentById(id, email)
        );
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(
            @PathVariable int id,
            Principal principal) {

        String email = principal.getName();

        documentService.deleteDocument(id, email);

        return ResponseEntity.ok("Document deleted successfully");
    }
}