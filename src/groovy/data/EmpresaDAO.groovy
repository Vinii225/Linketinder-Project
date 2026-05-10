package groovy.data

import groovy.data.contracts.EmpresaRepository
import groovy.data.executor.DatabaseExecutor
import groovy.data.strategy.SelectListStrategy
import groovy.data.strategy.SelectStrategy
import groovy.data.strategy.TransactionStrategy
import groovy.data.strategy.UpdateStrategy
import groovy.model.Empresa

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class EmpresaDAO implements EmpresaRepository {

    Empresa create(Empresa empresa) {
        String sql = """
            INSERT INTO empresa (nome_empresa, cnpj, email_corporativo, descricao_empresa, pais, cep, senha)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id_empresa
        """.stripIndent()

        def strategy = new TransactionStrategy<Empresa>({ Connection connection ->
            Integer idEmpresa = null
            
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setString(1, empresa.nomeEmpresa)
                statement.setString(2, empresa.cnpj)
                statement.setString(3, empresa.emailCorporativo)
                statement.setString(4, empresa.descricaoEmpresa)
                statement.setString(5, empresa.pais)
                statement.setString(6, empresa.cep)
                statement.setString(7, empresa.senha)

                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    resultSet.next()
                    idEmpresa = resultSet.getInt("id_empresa")
                }
            }

            empresa.idEmpresa = idEmpresa
            return empresa
        })

        return DatabaseExecutor.execute(strategy, sql, {})
    }

    List<Empresa> findAll() {
        String sql = """
            SELECT id_empresa, nome_empresa, cnpj, email_corporativo, descricao_empresa, pais, cep, senha
            FROM empresa
            ORDER BY id_empresa
        """.stripIndent()

        def strategy = new SelectListStrategy<Empresa>({ ResultSet rs -> mapEmpresa(rs) })
        
        return DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            // Sem parâmetros
        })
    }

    Empresa findById(Integer idEmpresa) {
        String sql = """
            SELECT id_empresa, nome_empresa, cnpj, email_corporativo, descricao_empresa, pais, cep, senha
            FROM empresa
            WHERE id_empresa = ?
        """.stripIndent()

        def strategy = new SelectStrategy<Empresa>({ ResultSet rs -> mapEmpresa(rs) })
        
        return DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            statement.setInt(1, idEmpresa)
        })
    }

    boolean update(Empresa empresa) {
        String sql = """
            UPDATE empresa
            SET nome_empresa = ?, cnpj = ?, email_corporativo = ?, descricao_empresa = ?, pais = ?, cep = ?, senha = ?
            WHERE id_empresa = ?
        """.stripIndent()

        def strategy = new UpdateStrategy()
        
        int updated = DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            statement.setString(1, empresa.nomeEmpresa)
            statement.setString(2, empresa.cnpj)
            statement.setString(3, empresa.emailCorporativo)
            statement.setString(4, empresa.descricaoEmpresa)
            statement.setString(5, empresa.pais)
            statement.setString(6, empresa.cep)
            statement.setString(7, empresa.senha)
            statement.setInt(8, empresa.idEmpresa)
        })

        return updated > 0
    }

    boolean delete(Integer idEmpresa) {
        String sql = "DELETE FROM empresa WHERE id_empresa = ?"

        def strategy = new UpdateStrategy()
        
        int deleted = DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            statement.setInt(1, idEmpresa)
        })

        return deleted > 0
    }

    private static Empresa mapEmpresa(ResultSet resultSet) {
        return new Empresa(
            idEmpresa: resultSet.getInt("id_empresa"),
            nomeEmpresa: resultSet.getString("nome_empresa"),
            cnpj: resultSet.getString("cnpj"),
            emailCorporativo: resultSet.getString("email_corporativo"),
            descricaoEmpresa: resultSet.getString("descricao_empresa"),
            pais: resultSet.getString("pais"),
            cep: resultSet.getString("cep"),
            senha: resultSet.getString("senha")
        )
    }
}
