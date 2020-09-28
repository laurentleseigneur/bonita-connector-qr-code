package com.bonitasoft.connectors

import com.google.zxing.BarcodeFormat
import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

import groovy.util.logging.Slf4j

@Slf4j
class BonitaQrCode extends AbstractConnector {

    def static final CODE_TYPE_INPUT = "codeType"

    def static final CONTENT_INPUT = "content"
    def static final FILENAME_INPUT = "filename"
    def static final SIZE_INPUT = "size"
    def static final WIDTH_INPUT = "width"
    def static final HEIGHT_INPUT = "height"

    def static final OUTPUT_DOCUMENT_VALUE = "outputDocumentValue"

    /**
     * Perform validation on the inputs defined on the connector definition (src/main/resources/bonita-qr-code.def)
     * You should:
     * - validate that mandatory inputs are presents
     * - validate that the content of the inputs is coherent with your use case (e.g: validate that a date is / isn't in the past ...)
     */
    @Override
    void validateInputParameters() throws ConnectorValidationException {
        checkMandatoryStringInput(CODE_TYPE_INPUT)
        checkMandatoryStringInput(CONTENT_INPUT)
        checkMandatoryStringInput(FILENAME_INPUT)
        def codeType = getInputParameter(CODE_TYPE_INPUT)
        switch (codeType) {
            case BarcodeFormat.QR_CODE.name():
                checkMandatoryIntegerInput(SIZE_INPUT)
                break
            case BarcodeFormat.CODE_128.name():
                checkMandatoryIntegerInput(WIDTH_INPUT)
                checkMandatoryIntegerInput(HEIGHT_INPUT)
                break
            default:
                throw new ConnectorValidationException(this, "Parameter $CODE_TYPE_INPUT accepts only '${BarcodeFormat.QR_CODE}' or '${BarcodeFormat.CODE_128}' value.")
        }
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
        def generatedCode
        def codeType = getInputParameter(CODE_TYPE_INPUT)
        def filename = getInputParameter(FILENAME_INPUT)

        switch (codeType) {
            case BarcodeFormat.QR_CODE.name():
                generatedCode = generateQrCode(filename)
                break
            case BarcodeFormat.CODE_128.name():
                generatedCode = generateCode128(filename)
                break
        }

        log.info("generate $codeType: $filename result based on ${generatedCode.getAbsolutePath()}")
        DocumentValue documentValue = new DocumentValue(generatedCode.bytes, "image/png", filename)

        setOutputParameter(OUTPUT_DOCUMENT_VALUE, documentValue)
    }

    File generateQrCode(def filename) {
        def content = getInputParameter(CONTENT_INPUT)

        def size = getInputParameter(SIZE_INPUT)
        log.info "$CONTENT_INPUT : $content"
        log.info "$SIZE_INPUT : $size"
        log.info "$FILENAME_INPUT : $filename"

        QrCodeGenerator generator = new QrCodeGenerator()
        File generatedCode = generator.generateQRCodeImage(content, size)
        generatedCode
    }

    File generateCode128(def filename) {
        def content = getInputParameter(CONTENT_INPUT)

        def width = getInputParameter(WIDTH_INPUT)
        def height = getInputParameter(HEIGHT_INPUT)

        log.info "$CONTENT_INPUT : $content"
        log.info "$WIDTH_INPUT : $width"
        log.info "$HEIGHT_INPUT : $height"
        log.info "$FILENAME_INPUT : $filename"

        Code128Generator generator = new Code128Generator()
        File generatedCode = generator.generateCode128Image(content, width, height)
        generatedCode
    }

    /**
     * [Optional] Open a connection to remote server
     */
    @Override
    void connect() throws ConnectorException {}

    /**
     * [Optional] Close connection to remote server
     */
    @Override
    void disconnect() throws ConnectorException {}
}
