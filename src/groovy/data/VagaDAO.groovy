package groovy.data

import groovy.data.contracts.VagaRepository
import groovy.data.executor.DatabaseExecutor
import groovy.data.strategy.SelectListStrategy
import groovy.data.strategy.SelectStrategy
import groovy.data.strategy.TransactionStrategy
import groovy.data.strategy.UpdateStrategy
import groovy.model.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class VagaDAO implements VagaRepository {

    Vaga create(Vaga vaga) {
        String sql = """
            INSERT INTO vaga (id_empresa, nome_vaga, descricao, localizacao)
            VALUES (?, ?, ?, ?)
            RETURNING id_vaga
        """.stripIndent()

        def strategy = new TransactionStrategy<Vaga>({ Connection connection ->
            Integer idVaga = null
            
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setInt(1, vaga.idEmpresa)
                statement.setString(2, vaga.nomeVaga)
                statement.setString(3, vaga.descricao)
                statement.setString(4, vaga.localizacao)

                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    resultSet.next()
                    idVaga = resultSet.getInt("id_vaga")
                }
            }
            
            vaga.idVaga = idVaga
            return vaga
        })

        return DatabaseExecutor.execute(strategy, sql, {})
    }

    List<Vaga> findAll() {
        String sql = """
            SELECT id_vaga, id_empresa, nome_vaga, descricao, localizacao
            FROM vaga
            ORDER BY id_vaga
        """.stripIndent()

        def strategy = new SelectListStrategy<Vaga>({ ResultSet rs -> mapVaga(rs) })
        
        return DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            // Sem parâmetros
        })
    }

    Vaga findById(Integer idVaga) {
        String sql = """
            SELECT id_vaga, id_empresa, nome_vaga, descricao, localizacao
            FROM vaga
            WHERE id_vaga = ?
        """.stripIndent()

        def strategy = new SelectStrategy<Vaga>({ ResultSet rs -> mapVaga(rs) })
        
        return DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            statement.setInt(1, idVaga)
        })
    }

    boolean update(Vaga vaga) {
        String sql = """
            UPDATE vaga
            SET id_empresa = ?, nome_vaga = ?, descricao = ?, localizacao = ?
            WHERE id_vaga = ?
        """.stripIndent()

        def strategy = new UpdateStrategy()
        
        int updated = DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            statement.setInt(1, vaga.idEmpresa)
            statement.setString(2, vaga.nomeVaga)
            statement.setString(3, vaga.descricao)
            statement.setString(4, vaga.localizacao)
            statement.setInt(5, vaga.idVaga)
        })

        return updated > 0
    }

    boolean delete(Integer idVaga) {
        String sql = "DELETE FROM vaga WHERE id_vaga = ?"

        def strategy = new UpdateStrategy()
        
        int deleted = DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            statement.setInt(1, idVaga)
        })

        return deleted > 0
    }

    private static Vaga mapVaga(ResultSet resultSet) {
        return new Vaga(
            idVaga: resultSet.getInt("id_vaga"),
            idEmpresa: resultSet.getInt("id_empresa"),
            nomeVaga: resultSet.getString("nome_vaga"),
            descricao: resultSet.getString("descricao"),
            localizacao: resultSet.getString("localizacao")
        )
    }
}
