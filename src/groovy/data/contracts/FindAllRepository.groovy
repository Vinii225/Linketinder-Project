package groovy.data.contracts

interface FindAllRepository<T> {
    List<T> findAll()
}
