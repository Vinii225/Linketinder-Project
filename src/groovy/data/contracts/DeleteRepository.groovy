package groovy.data.contracts

interface DeleteRepository<ID> {
    boolean delete(ID id)
}
