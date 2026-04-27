package groovy.data.contracts

import groovy.model.Empresa

interface EmpresaRepository extends
    CreateRepository<Empresa>,
    UpdateRepository<Empresa>,
    DeleteRepository<Integer>,
    FindAllRepository<Empresa>,
    FindByIdRepository<Empresa, Integer> {
}
