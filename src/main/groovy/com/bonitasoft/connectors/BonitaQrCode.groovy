package com.bonitasoft.connectors

import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

import groovy.util.logging.Slf4j

@Slf4j
class BonitaQrCode extends AbstractConnector {
    
    def static final CONTENT_INPUT = "content"
    def static final FILENAME_INPUT = "filename"
    def static final SIZE_INPUT = "size"

    def static final OUTPUT_DOCUMENT_VALUE = "outputDocumentValue"
    
    /**
     * Perform validation on the inputs defined on the connector definition (src/main/resources/bonita-qr-code.def)
     * You should:
     * - validate that mandatory inputs are presents
     * - validate that the content of the inputs is coherent with your use case (e.g: validate that a date is / isn't in the past ...)
     */
    @Override
    void validateInputParameters() throws ConnectorValidationException {
        checkMandatoryStringInput(CONTENT_INPUT)
        checkMandatoryStringInput(FILENAME_INPUT)
        checkMandatoryIntegerInput(SIZE_INPUT)
    }
    
    def checkMandatoryStringInput(inputName) throws ConnectorValidationException {
        def value = getInputParameter(inputName)
        if (value in String) {
            if (!value) {
                throw new ConnectorValidationException(this, "Mandatory parameter '$inputName' is missing.")
            }
        } else {
            throw new ConnectorValidationException(this, "'$inputName' parameter must be a String")
        }
    }

    def checkMandatoryIntegerInput(inputName) throws ConnectorValidationException {
        def value = getInputParameter(inputName)
        if (value in Integer) {
            if (!value) {
                throw new ConnectorValidationException(this, "Mandatory parameter '$inputName' is missing.")
            }
        } else {
            throw new ConnectorValidationException(this, "'$inputName' parameter must be an Integer")
        }
    }

    /**
     * Core method:
     * - Execute all the business logic of your connector using the inputs (connect to an external service, compute some values ...).
     * - Set the output of the connector execution. If outputs are not set, connector fails.
     */
    @Override
    void executeBusinessLogic() throws ConnectorException {
        def content = getInputParameter(CONTENT_INPUT)
        def fileName = getInputParameter(FILENAME_INPUT)
        def size = getInputParameter(SIZE_INPUT)
        log.info "$CONTENT_INPUT : $content"
        log.info "$SIZE_INPUT : $size"

        QrCodeGenerator generator=new QrCodeGenerator()
        def qrCode = generator.generateQRCodeImage(content, size)
        DocumentValue documentValue= new DocumentValue(qrCode.bytes,"image/png",fileName)

        setOutputParameter(OUTPUT_DOCUMENT_VALUE, documentValue)
    }
    
    /**
     * [Optional] Open a connection to remote server
     */
    @Override
    def void connect() throws ConnectorException{}

    /**
     * [Optional] Close connection to remote server
     */
    @Override
    def void disconnect() throws ConnectorException{}
}
