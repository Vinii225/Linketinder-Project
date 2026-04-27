package groovy.data.contracts

interface FindByIdRepository<T, ID> {
    T findById(ID id)
}
