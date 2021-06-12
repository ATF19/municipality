package com.municipality.backend.application.utility

import org.owasp.html.HtmlPolicyBuilder

class HtmlSanitizer private constructor() {

    companion object {
        fun sanitze(untrustedHtml: String): String {
            val policyFactory = HtmlPolicyBuilder()
                .allowAttributes("style").globally()
                .allowAttributes("src").onElements("img")
                .allowAttributes("href").onElements("a")
                .allowStandardUrlProtocols()
                .allowElements("a", "img", "h1", "h2", "h3", "h4", "h5", "h6", "p")
                .toFactory()

            return policyFactory.sanitize(untrustedHtml)
        }
    }

}