package groovy.data.contracts

import groovy.model.Competencia

interface CompetenciaRepository extends
    CreateRepository<Competencia>,
    UpdateRepository<Competencia>,
    DeleteRepository<Integer>,
    FindAllRepository<Competencia> {
}
