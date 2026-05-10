package groovy.data.executor

import groovy.data.pool.DatabaseConnectionPool
import groovy.data.strategy.ExecutionStrategy

class DatabaseExecutor {
    static <T> T execute(ExecutionStrategy<T> strategy, String sql, Closure<?> bind = {}) {
        DatabaseConnectionPool.getInstance().getConnection().withCloseable { connection ->
            strategy.execute(connection, sql, bind)
        }
    }
}
