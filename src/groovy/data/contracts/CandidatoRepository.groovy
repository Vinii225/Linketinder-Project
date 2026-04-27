package groovy.data.contracts

import groovy.model.Candidato

interface CandidatoRepository extends
    CreateRepository<Candidato>,
    UpdateRepository<Candidato>,
    DeleteRepository<Integer>,
    FindAllRepository<Candidato>,
    FindByIdRepository<Candidato, Integer> {
}
