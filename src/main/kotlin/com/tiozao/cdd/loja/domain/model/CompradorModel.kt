package com.tiozao.cdd.loja.domain.model

import com.tiozao.cdd.loja.domain.model.validators.CPF_OR_CNPJ
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class CompradorModel(
    var id: Int?,
    @field:Email
    @field:NotNull
    var email: String,
    @field:NotNull
    var nome: String,
    @field:NotNull
    var sobrenome: String,
    @field:CPF_OR_CNPJ
    var documento: String,
    @field:NotNull
    var endereco: String,
    @field:NotNull
    var complemento: String,
    @field:NotNull
    var cidade: String,
    @field:NotNull
    var paisId: Int,
    var estadoId: Int?,
    @field:NotNull
    var telefone: String?,
    @field:NotNull
    var cep: String?
)