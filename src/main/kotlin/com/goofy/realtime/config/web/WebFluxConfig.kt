package com.goofy.realtime.config.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.goofy.realtime.common.model.DelegatedValue
import com.goofy.realtime.domain.trend.vo.TrendId
import org.springframework.context.annotation.Configuration
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.core.convert.converter.Converter
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.util.MimeType
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer
import java.nio.charset.Charset

@Configuration
class WebFluxConfig(
    private val objectMapper: ObjectMapper,
) : WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns(CorsConfiguration.ALL)
            .allowedMethods(CorsConfiguration.ALL)
            .allowedHeaders(CorsConfiguration.ALL)
            .allowCredentials(true)
            .maxAge(3600)
    }

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        val mimeTypes = arrayOf(
            MimeType("application", "json"),
            MimeType("application", "*+json"),
            MimeType("application", "json", Charset.forName("UTF-8"))
        )

        configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, *mimeTypes))
        configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
    }

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        val registry = ReactiveAdapterRegistry()
        val serverCodecConfigurer = ServerCodecConfigurer.create()
        configureHttpMessageCodecs(serverCodecConfigurer)

        configurer.addCustomResolver(
            ReactiveSortHandlerMethodArgumentResolver(),
            ReactivePageableHandlerMethodArgumentResolver()
        )
    }

    override fun addFormatters(registry: FormatterRegistry) {
        val registrar = DateTimeFormatterRegistrar()
        registrar.setUseIsoFormat(true)
        registrar.registerFormatters(registry)
        super.addFormatters(registry)

        registry.addConverter(
            String::class.java,
            TrendId::class.java,
            StringToDelegatedLongValueConverter(::TrendId),
        )
    }
}

class StringToDelegatedLongValueConverter<T : DelegatedValue<Long>>(
    private val factory: (Long) -> T
) : Converter<String, T> {
    override fun convert(source: String): T {
        return factory(source.toLong())
    }
}
