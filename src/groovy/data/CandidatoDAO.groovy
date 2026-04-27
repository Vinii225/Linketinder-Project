package groovy.data

import groovy.model.Candidato

import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet

class CandidatoDAO {
    private final CompetenciaDAO competenciaDAO = new CompetenciaDAO()

    Candidato create(Candidato candidato) {
        String sql = """
            INSERT INTO candidato (nome, sobrenome, data_nasc, email, cpf, pais, cep, descricao_pessoal, senha)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id_candidato
        """.stripIndent()

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.setAutoCommit(false)

            try {
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
                        candidato.idCandidato = resultSet.getInt("id_candidato")
                    }
                }

                saveCompetencias(connection, candidato.idCandidato, candidato.competencias)

                connection.commit()
                return candidato
            } catch (Exception exception) {
                connection.rollback()
                throw exception
            }
        }
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

        List<Candidato> candidatos = []

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    while (resultSet.next()) {
                        candidatos << mapCandidato(resultSet)
                    }
                }
            }
        }

        return candidatos
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

        Candidato candidato = null

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setInt(1, idCandidato)

                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    if (resultSet.next()) {
                        candidato = mapCandidato(resultSet)
                    }
                }
            }
        }

        return candidato
    }

    boolean update(Candidato candidato) {
        String sql = """
            UPDATE candidato
            SET nome = ?, sobrenome = ?, data_nasc = ?, email = ?, cpf = ?, pais = ?, cep = ?, descricao_pessoal = ?, senha = ?
            WHERE id_candidato = ?
        """.stripIndent()

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.setAutoCommit(false)

            try {
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

                connection.commit()
                return updated > 0
            } catch (Exception exception) {
                connection.rollback()
                throw exception
            }
        }
    }

    boolean delete(Integer idCandidato) {
        String sql = "DELETE FROM candidato WHERE id_candidato = ?"

        int deleted = 0

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setInt(1, idCandidato)
                deleted = statement.executeUpdate()
            }
        }

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
                    int idCompetencia = competenciaDAO.findOrCreateByName(nomeCompetencia, connection)
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
