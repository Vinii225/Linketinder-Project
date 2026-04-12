package groovy.data

import groovy.model.Competencia

import java.sql.Connection
import java.sql.PreparedStatement

class CompetenciaDAO {

    List<Competencia> findAll() {
        String sql = "SELECT id_competencia, nome_competencia FROM competencia ORDER BY id_competencia"
        List<Competencia> competencias = []

        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        def resultSet = statement.executeQuery()

        while (resultSet.next()) {
            competencias << new Competencia(
                idCompetencia: resultSet.getInt("id_competencia"),
                nomeCompetencia: resultSet.getString("nome_competencia")
            )
        }

        resultSet.close()
        statement.close()
        connection.close()
        return competencias
    }

    Competencia create(Competencia competencia) {
        String sql = "INSERT INTO competencia (nome_competencia) VALUES (?) RETURNING id_competencia"

        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        statement.setString(1, competencia.nomeCompetencia)

        def resultSet = statement.executeQuery()
        if (resultSet.next()) {
            competencia.idCompetencia = resultSet.getInt("id_competencia")
        }

        resultSet.close()
        statement.close()
        connection.close()
        return competencia
    }

    boolean update(Competencia competencia) {
        String sql = "UPDATE competencia SET nome_competencia = ? WHERE id_competencia = ?"

        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        statement.setString(1, competencia.nomeCompetencia)
        statement.setInt(2, competencia.idCompetencia)

        int updated = statement.executeUpdate()
        statement.close()
        connection.close()
        return updated > 0
    }

    boolean delete(Integer idCompetencia) {
        String sql = "DELETE FROM competencia WHERE id_competencia = ?"

        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        statement.setInt(1, idCompetencia)

        int deleted = statement.executeUpdate()
        statement.close()
        connection.close()
        return deleted > 0
    }

    Integer findOrCreateByName(String nomeCompetencia, Connection connection) {
        String findSql = "SELECT id_competencia FROM competencia WHERE LOWER(nome_competencia) = LOWER(?)"
        PreparedStatement findStatement = connection.prepareStatement(findSql)
        findStatement.setString(1, nomeCompetencia)
        def resultSet = findStatement.executeQuery()

        if (resultSet.next()) {
            int existingId = resultSet.getInt("id_competencia")
            resultSet.close()
            findStatement.close()
            return existingId
        }

        resultSet.close()
        findStatement.close()

        String createSql = "INSERT INTO competencia (nome_competencia) VALUES (?) RETURNING id_competencia"
        PreparedStatement createStatement = connection.prepareStatement(createSql)
        createStatement.setString(1, nomeCompetencia)
        def createResult = createStatement.executeQuery()

        createResult.next()
        int newId = createResult.getInt("id_competencia")
        createResult.close()
        createStatement.close()
        return newId
    }
}
