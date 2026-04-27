package groovy.data.contracts

import groovy.model.Vaga

interface VagaRepository extends
    CreateRepository<Vaga>,
    UpdateRepository<Vaga>,
    DeleteRepository<Integer>,
    FindAllRepository<Vaga>,
    FindByIdRepository<Vaga, Integer> {
}
