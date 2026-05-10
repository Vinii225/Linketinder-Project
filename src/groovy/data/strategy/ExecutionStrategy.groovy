package groovy.data.strategy

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

interface ExecutionStrategy<T> {
    T execute(Connection connection, String sql, Closure<?> bind)
}

class SelectStrategy<T> implements ExecutionStrategy<T> {
    private final Closure<T> mapper
    
    SelectStrategy(Closure<T> mapper) {
        this.mapper = mapper
    }
    
    @Override
    T execute(Connection connection, String sql, Closure<?> bind) {
        List<T> results = []
        
        connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
            bind.call(statement)
            statement.executeQuery().withCloseable { ResultSet resultSet ->
                while (resultSet.next()) {
                    results << mapper.call(resultSet)
                }
            }
        }
        
        return results.isEmpty() ? null : results[0]
    }
}

class SelectListStrategy<T> implements ExecutionStrategy<List<T>> {
    private final Closure<T> mapper
    
    SelectListStrategy(Closure<T> mapper) {
        this.mapper = mapper
    }
    
    @Override
    List<T> execute(Connection connection, String sql, Closure<?> bind) {
        List<T> results = []
        
        connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
            bind.call(statement)
            statement.executeQuery().withCloseable { ResultSet resultSet ->
                while (resultSet.next()) {
                    results << mapper.call(resultSet)
                }
            }
        }
        
        return results
    }
}

class UpdateStrategy implements ExecutionStrategy<Integer> {
    @Override
    Integer execute(Connection connection, String sql, Closure<?> bind) {
        connection.setAutoCommit(false)
        
        try {
            int rowsAffected = 0
            
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                bind.call(statement)
                rowsAffected = statement.executeUpdate()
            }
            
            connection.commit()
            return rowsAffected
        } catch (Exception e) {
            connection.rollback()
            throw e
        }
    }
}

class TransactionStrategy<T> implements ExecutionStrategy<T> {
    private final Closure<T> transaction
    
    TransactionStrategy(Closure<T> transaction) {
        this.transaction = transaction
    }
    
    @Override
    T execute(Connection connection, String sql, Closure<?> bind) {
        connection.setAutoCommit(false)
        
        try {
            T result = transaction.call(connection)
            connection.commit()
            return result
        } catch (Exception e) {
            connection.rollback()
            throw e
        }
    }
}
