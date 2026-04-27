package groovy.data.contracts

interface UpdateRepository<T> {
    boolean update(T entity)
}
