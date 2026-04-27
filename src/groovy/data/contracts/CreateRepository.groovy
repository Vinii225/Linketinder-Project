package groovy.data.contracts

interface CreateRepository<T> {
    T create(T entity)
}
