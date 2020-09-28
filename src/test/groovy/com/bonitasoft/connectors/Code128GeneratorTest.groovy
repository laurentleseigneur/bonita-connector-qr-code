package com.bonitasoft.connectors

import spock.lang.Specification

class Code128GeneratorTest extends Specification {

    def "should generate a code128 code"() {
        given:
        File expected
        Code128Generator codeGenerator = new Code128Generator()

        when:
        expected = codeGenerator.generateCode128Image("1234567984564132132158748648", 500,50)

        then:
        expected.exists()
        expected.canRead()
    }
}
