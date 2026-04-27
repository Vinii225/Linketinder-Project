package groovy.data

import groovy.data.contracts.CompetenciaRepository
import groovy.data.contracts.CompetenciaLookup
import groovy.model.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class CompetenciaDAO implements CompetenciaRepository, CompetenciaLookup {

    List<Competencia> findAll() {
        String sql = "SELECT id_competencia, nome_competencia FROM competencia ORDER BY id_competencia"
        List<Competencia> competencias = []

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    while (resultSet.next()) {
                        competencias << mapCompetencia(resultSet)
                    }
                }
            }
        }

        return competencias
    }

    Competencia create(Competencia competencia) {
        String sql = "INSERT INTO competencia (nome_competencia) VALUES (?) RETURNING id_competencia"

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setString(1, competencia.nomeCompetencia)

                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    if (resultSet.next()) {
                        competencia.idCompetencia = resultSet.getInt("id_competencia")
                    }
                }
            }
        }

        return competencia
    }

    boolean update(Competencia competencia) {
        String sql = "UPDATE competencia SET nome_competencia = ? WHERE id_competencia = ?"

        int updated = 0

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setString(1, competencia.nomeCompetencia)
                statement.setInt(2, competencia.idCompetencia)
                updated = statement.executeUpdate()
            }
        }

        return updated > 0
    }

    boolean delete(Integer idCompetencia) {
        String sql = "DELETE FROM competencia WHERE id_competencia = ?"

        int deleted = 0

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setInt(1, idCompetencia)
                deleted = statement.executeUpdate()
            }
        }

        return deleted > 0
    }

    Integer findOrCreateByName(String nomeCompetencia, Connection connection) {
        String findSql = "SELECT id_competencia FROM competencia WHERE LOWER(nome_competencia) = LOWER(?)"
        connection.prepareStatement(findSql).withCloseable { PreparedStatement findStatement ->
            findStatement.setString(1, nomeCompetencia)

            findStatement.executeQuery().withCloseable { ResultSet resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt("id_competencia")
                }
            }
        }

        String createSql = "INSERT INTO competencia (nome_competencia) VALUES (?) RETURNING id_competencia"
        int newId = 0

        connection.prepareStatement(createSql).withCloseable { PreparedStatement createStatement ->
            createStatement.setString(1, nomeCompetencia)

            createStatement.executeQuery().withCloseable { ResultSet createResult ->
                createResult.next()
                newId = createResult.getInt("id_competencia")
            }
        }

        return newId
    }

    private static Competencia mapCompetencia(ResultSet resultSet) {
        return new Competencia(
            idCompetencia: resultSet.getInt("id_competencia"),
            nomeCompetencia: resultSet.getString("nome_competencia")
        )
    }
}
