package com.tiozao.cdd.loja.domain.service

import com.tiozao.cdd.loja.domain.model.CarrinhoCompraModel
import com.tiozao.cdd.loja.repository.*
import com.tiozao.cdd.loja.repository.entity.CarrinhoCompraEntity
import com.tiozao.cdd.loja.repository.entity.CarrinhoCompraItemEntity
import com.tiozao.cdd.loja.repository.entity.EstadoEntity
import com.tiozao.cdd.loja.repository.extensions.toEntity
import com.tiozao.cdd.loja.repository.extensions.toModel
import org.hibernate.validator.internal.engine.ConstraintViolationImpl
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import java.lang.reflect.Parameter
import java.math.BigDecimal
import javax.management.InvalidAttributeValueException
import javax.transaction.Transactional
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Valid

@Service
@Validated
class CarrinhoCompraService(
    private var carrinhoComprasRepository: CarrinhoComprasRepository,
    private var compradorRepository: CompradorRepository,
    private var livroRepository: LivroRepository,
    private var paisRepository: PaisRepository,
    private var estadoRepository: EstadoRepository,
    private var carrinhoCompraItemRepository: CarrinhoCompraItenRepository) {

    @Transactional
    fun createCarrinhoCompra(@Valid carrinho : CarrinhoCompraModel): CarrinhoCompraModel {

       var itens: MutableList<CarrinhoCompraItemEntity> = mutableListOf()
        var total: BigDecimal = BigDecimal(0)
        for(item in carrinho.itens){
            var livro = livroRepository.findById(item.idLivro).get()
            var itemEntity= CarrinhoCompraItemEntity(
                id = null,
                livro = livro,
                quantidade = item.quantidade,
                carrinhoCompra = null
            )
            itens.add(
                itemEntity
            )
            total = total.plus(livro.preco!!.multiply(BigDecimal(item.quantidade)))
        }
        if(total != carrinho.total){
            throw InvalidAttributeValueException("valor total invalido.")
        }

        var carrinhoEntity = carrinhoComprasRepository.save(carrinho.toEntity(compradorRepository.save(
            carrinho.comprador.toEntity(
                paisRepository.findById(carrinho.comprador.paisId).get(),
                getEstado(carrinho.comprador.estadoId)
            )
        ), null))
        itens.forEach {
            it.carrinhoCompra = carrinhoEntity
            carrinhoCompraItemRepository.save(it)
        }
        carrinhoCompraItemRepository.saveAll(itens)
        return carrinhoEntity.copy(itens = itens ).toModel()
    }

    private fun getEstado(estadoId: Int?) =
        estadoId?.let {
            estadoRepository.findById(estadoId).orElse(null)
        }.run {
            null
        }

}