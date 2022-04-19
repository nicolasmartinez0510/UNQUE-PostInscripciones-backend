package ar.edu.unq.postinscripciones.model.oferta.academica

import ar.edu.unq.postinscripciones.model.comision.Comision
import java.time.Year
import javax.persistence.*

@Entity
@IdClass(OfertaAcademicaId::class)
class OfertaAcademica(
    @Id
    val anio: Year,
    @Id
    val cuatrimestre: Cuatrimestre,
    @OneToMany(fetch = FetchType.LAZY)
    val oferta: List<Comision>
)
