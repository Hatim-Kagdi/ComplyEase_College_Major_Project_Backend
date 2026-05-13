package in.complyease.dto.document;

import in.complyease.enums.DocumentType;
import lombok.*;

@Getter
@AllArgsConstructor
public class DocumentResponse {

    private int documentId;

    private int businessId;
    private Integer complianceId;
    private String businessName;

    private String fileName;
    private String fileUrl;

    private DocumentType documentType;


}