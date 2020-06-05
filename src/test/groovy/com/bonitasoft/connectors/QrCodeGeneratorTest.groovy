package com.bonitasoft.connectors

import spock.lang.Specification

class QrCodeGeneratorTest extends Specification {

    def "should generate qr code"() {
        given:
        File expected
        QrCodeGenerator codeGenerator = new QrCodeGenerator()

        when:
        expected = codeGenerator.generateQRCodeImage("Lorem", 200)

        then:
        expected.exists()
        expected.canRead()
    }
}
