package groovy.data

import groovy.data.contracts.CandidatoRepository
import groovy.data.contracts.CompetenciaLookup
import groovy.data.executor.DatabaseExecutor
import groovy.data.strategy.SelectListStrategy
import groovy.data.strategy.SelectStrategy
import groovy.data.strategy.TransactionStrategy
import groovy.data.strategy.UpdateStrategy
import groovy.model.Candidato

import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet

class CandidatoDAO implements CandidatoRepository {
    private final CompetenciaLookup competenciaLookup

    CandidatoDAO() {
        this(new CompetenciaDAO())
    }

    CandidatoDAO(CompetenciaLookup competenciaLookup) {
        this.competenciaLookup = competenciaLookup
    }

    Candidato create(Candidato candidato) {
        String sql = """
            INSERT INTO candidato (nome, sobrenome, data_nasc, email, cpf, pais, cep, descricao_pessoal, senha)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id_candidato
        """.stripIndent()

        def strategy = new TransactionStrategy<Candidato>({ Connection connection ->
            Integer idCandidato = null
            
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setString(1, candidato.nome)
                statement.setString(2, candidato.sobrenome)
                statement.setDate(3, Date.valueOf(candidato.dataNasc))
                statement.setString(4, candidato.email)
                statement.setString(5, candidato.cpf)
                statement.setString(6, candidato.pais)
                statement.setString(7, candidato.cep)
                statement.setString(8, candidato.descricaoPessoal)
                statement.setString(9, candidato.senha)

                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    resultSet.next()
                    idCandidato = resultSet.getInt("id_candidato")
                }
            }

            candidato.idCandidato = idCandidato
            saveCompetencias(connection, candidato.idCandidato, candidato.competencias)
            
            return candidato
        })

        return DatabaseExecutor.execute(strategy, sql, {})
    }

    List<Candidato> findAll() {
        String sql = """
            SELECT c.id_candidato, c.nome, c.sobrenome, c.data_nasc, c.email, c.cpf, c.pais, c.cep, c.descricao_pessoal, c.senha,
                   COALESCE(string_agg(comp.nome_competencia, ', ' ORDER BY comp.nome_competencia), '') AS competencias
            FROM candidato c
            LEFT JOIN candidato_competencia cc ON c.id_candidato = cc.id_candidato
            LEFT JOIN competencia comp ON comp.id_competencia = cc.id_competencia
            GROUP BY c.id_candidato
            ORDER BY c.id_candidato
        """.stripIndent()

        def strategy = new SelectListStrategy<Candidato>({ ResultSet rs -> mapCandidato(rs) })
        
        return DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            // Sem parâmetros
        })
    }

    Candidato findById(Integer idCandidato) {
        String sql = """
            SELECT c.id_candidato, c.nome, c.sobrenome, c.data_nasc, c.email, c.cpf, c.pais, c.cep, c.descricao_pessoal, c.senha,
                   COALESCE(string_agg(comp.nome_competencia, ', ' ORDER BY comp.nome_competencia), '') AS competencias
            FROM candidato c
            LEFT JOIN candidato_competencia cc ON c.id_candidato = cc.id_candidato
            LEFT JOIN competencia comp ON comp.id_competencia = cc.id_competencia
            WHERE c.id_candidato = ?
            GROUP BY c.id_candidato
        """.stripIndent()

        def strategy = new SelectStrategy<Candidato>({ ResultSet rs -> mapCandidato(rs) })
        
        return DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            statement.setInt(1, idCandidato)
        })
    }

    boolean update(Candidato candidato) {
        String sql = """
            UPDATE candidato
            SET nome = ?, sobrenome = ?, data_nasc = ?, email = ?, cpf = ?, pais = ?, cep = ?, descricao_pessoal = ?, senha = ?
            WHERE id_candidato = ?
        """.stripIndent()

        def strategy = new TransactionStrategy<Boolean>({ Connection connection ->
            int updated = 0
            
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setString(1, candidato.nome)
                statement.setString(2, candidato.sobrenome)
                statement.setDate(3, Date.valueOf(candidato.dataNasc))
                statement.setString(4, candidato.email)
                statement.setString(5, candidato.cpf)
                statement.setString(6, candidato.pais)
                statement.setString(7, candidato.cep)
                statement.setString(8, candidato.descricaoPessoal)
                statement.setString(9, candidato.senha)
                statement.setInt(10, candidato.idCandidato)

                updated = statement.executeUpdate()
            }

            clearCompetencias(connection, candidato.idCandidato)
            saveCompetencias(connection, candidato.idCandidato, candidato.competencias)

            return updated > 0
        })

        return DatabaseExecutor.execute(strategy, sql, {})
    }

    boolean delete(Integer idCandidato) {
        String sql = "DELETE FROM candidato WHERE id_candidato = ?"

        def strategy = new UpdateStrategy()
        
        int deleted = DatabaseExecutor.execute(strategy, sql, { PreparedStatement statement ->
            statement.setInt(1, idCandidato)
        })

        return deleted > 0
    }

    private void clearCompetencias(Connection connection, Integer idCandidato) {
        String sql = "DELETE FROM candidato_competencia WHERE id_candidato = ?"

        connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
            statement.setInt(1, idCandidato)
            statement.executeUpdate()
        }
    }

    private void saveCompetencias(Connection connection, Integer idCandidato, List<String> competencias) {
        if (!competencias) {
            return
        }

        String sql = "INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (?, ?) ON CONFLICT DO NOTHING"

        connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
            competencias
                .findAll { it != null && !it.trim().isEmpty() }
                .collect { it.trim() }
                .unique()
                .each { nomeCompetencia ->
                    int idCompetencia = competenciaLookup.findOrCreateByName(nomeCompetencia, connection)
                    statement.setInt(1, idCandidato)
                    statement.setInt(2, idCompetencia)
                    statement.addBatch()
                }

            statement.executeBatch()
        }
    }

    private static Candidato mapCandidato(ResultSet resultSet) {
        String skillsRaw = resultSet.getString("competencias")
        List<String> skills = skillsRaw ? skillsRaw.split(",").collect { it.trim() } : []

        return new Candidato(
            idCandidato: resultSet.getInt("id_candidato"),
            nome: resultSet.getString("nome"),
            sobrenome: resultSet.getString("sobrenome"),
            dataNasc: resultSet.getDate("data_nasc")?.toLocalDate(),
            email: resultSet.getString("email"),
            cpf: resultSet.getString("cpf"),
            pais: resultSet.getString("pais"),
            cep: resultSet.getString("cep"),
            descricaoPessoal: resultSet.getString("descricao_pessoal"),
            senha: resultSet.getString("senha"),
            competencias: skills
        )
    }
}
