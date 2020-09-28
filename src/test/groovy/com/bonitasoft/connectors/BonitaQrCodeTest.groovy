package com.bonitasoft.connectors

import com.google.zxing.BarcodeFormat
import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.connector.ConnectorValidationException

import spock.lang.Specification

class BonitaQrCodeTest extends Specification {

    def should_throw_exception_if_mandatory_input_is_missing() {
        given: 'A connector without input'
        def connector = new BonitaQrCode()

        when: 'Validating inputs'
        connector.validateInputParameters()

        then: 'ConnectorValidationException is thrown'
        thrown ConnectorValidationException
    }

    def should_throw_exception_if_mandatory_input_is_empty() {
        given: 'A connector without an empty input'
        def connector = new BonitaQrCode()
        connector.setInputParameters([(BonitaQrCode.CONTENT_INPUT): ''])

        when: 'Validating inputs'
        connector.validateInputParameters()

        then: 'ConnectorValidationException is thrown'
        thrown ConnectorValidationException
    }

    def should_throw_exception_if_mandatory_input_is_not_a_string() {
        given: 'A connector without an integer input'
        def connector = new BonitaQrCode()
        connector.setInputParameters([(BonitaQrCode.CONTENT_INPUT): 38])

        when: 'Validating inputs'
        connector.validateInputParameters()

        then: 'ConnectorValidationException is thrown'
        thrown ConnectorValidationException
    }

    def should_create_Qr_output_for_valid_input() {
        given: 'A connector with a valid input'
        def connector = new BonitaQrCode()
        connector.setInputParameters([
                (BonitaQrCode.CODE_TYPE_INPUT): BarcodeFormat.QR_CODE.name(),
                (BonitaQrCode.CONTENT_INPUT)  : 'valid',
                (BonitaQrCode.FILENAME_INPUT) : 'fileName',
                (BonitaQrCode.SIZE_INPUT)     : 350])

        when: 'Executing connector'
        def outputs = connector.execute()

        then: 'Output is created'
        outputs[(BonitaQrCode.OUTPUT_DOCUMENT_VALUE)].class == DocumentValue.class
    }

    def should_create_code128_output_for_valid_input() {
        given: 'A connector with a valid input'
        def connector = new BonitaQrCode()
        connector.setInputParameters([
                (BonitaQrCode.CODE_TYPE_INPUT): BarcodeFormat.CODE_128.name(),
                (BonitaQrCode.CONTENT_INPUT)  : '123456789',
                (BonitaQrCode.FILENAME_INPUT) : 'fileName',
                (BonitaQrCode.WIDTH_INPUT)    : 350,
                (BonitaQrCode.HEIGHT_INPUT)   : 50,
        ])

        when: 'Executing connector'
        def outputs = connector.execute()

        then: 'Output is created'
        outputs[(BonitaQrCode.OUTPUT_DOCUMENT_VALUE)].class == DocumentValue.class
    }
}