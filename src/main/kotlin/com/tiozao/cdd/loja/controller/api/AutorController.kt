package com.tiozao.cdd.loja.controller.api

import com.tiozao.cdd.loja.domain.model.AutorModel
import com.tiozao.cdd.loja.domain.service.AutorService

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@Validated
class AutorController(private var autorService: AutorService) {

    @PostMapping("/autor")
    fun createAutor(@Valid @RequestBody autor: AutorModel): ResponseEntity<AutorModel>{
        return ResponseEntity.ok(
            autorService.createAutor(
                autor))
    }

}