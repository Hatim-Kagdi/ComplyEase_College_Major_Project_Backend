package in.complyease.dto.document;

import in.complyease.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter

public class DocumentRequest {

    @NotNull(message = "Business ID is required")
    private Integer businessId;

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotBlank(message = "File URL is required")
    private String fileUrl;

    @NotNull(message = "Document type is required")
    private DocumentType documentType;
}