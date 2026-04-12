package groovy.data

import groovy.model.Vaga

import java.sql.Connection
import java.sql.PreparedStatement

class VagaDAO {

    Vaga create(Vaga vaga) {
        String sql = """
            INSERT INTO vaga (id_empresa, nome_vaga, descricao, localizacao)
            VALUES (?, ?, ?, ?)
            RETURNING id_vaga
        """.stripIndent()

        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        statement.setInt(1, vaga.idEmpresa)
        statement.setString(2, vaga.nomeVaga)
        statement.setString(3, vaga.descricao)
        statement.setString(4, vaga.localizacao)

        def resultSet = statement.executeQuery()
        resultSet.next()
        vaga.idVaga = resultSet.getInt("id_vaga")

        resultSet.close()
        statement.close()
        connection.close()
        return vaga
    }

    List<Vaga> findAll() {
        String sql = """
            SELECT id_vaga, id_empresa, nome_vaga, descricao, localizacao
            FROM vaga
            ORDER BY id_vaga
        """.stripIndent()

        List<Vaga> vagas = []
        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        def resultSet = statement.executeQuery()

        while (resultSet.next()) {
            vagas << new Vaga(
                idVaga: resultSet.getInt("id_vaga"),
                idEmpresa: resultSet.getInt("id_empresa"),
                nomeVaga: resultSet.getString("nome_vaga"),
                descricao: resultSet.getString("descricao"),
                localizacao: resultSet.getString("localizacao")
            )
        }

        resultSet.close()
        statement.close()
        connection.close()
        return vagas
    }

    Vaga findById(Integer idVaga) {
        String sql = """
            SELECT id_vaga, id_empresa, nome_vaga, descricao, localizacao
            FROM vaga
            WHERE id_vaga = ?
        """.stripIndent()

        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        statement.setInt(1, idVaga)

        def resultSet = statement.executeQuery()
        Vaga vaga = null

        if (resultSet.next()) {
            vaga = new Vaga(
                idVaga: resultSet.getInt("id_vaga"),
                idEmpresa: resultSet.getInt("id_empresa"),
                nomeVaga: resultSet.getString("nome_vaga"),
                descricao: resultSet.getString("descricao"),
                localizacao: resultSet.getString("localizacao")
            )
        }

        resultSet.close()
        statement.close()
        connection.close()
        return vaga
    }

    boolean update(Vaga vaga) {
        String sql = """
            UPDATE vaga
            SET id_empresa = ?, nome_vaga = ?, descricao = ?, localizacao = ?
            WHERE id_vaga = ?
        """.stripIndent()

        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        statement.setInt(1, vaga.idEmpresa)
        statement.setString(2, vaga.nomeVaga)
        statement.setString(3, vaga.descricao)
        statement.setString(4, vaga.localizacao)
        statement.setInt(5, vaga.idVaga)

        int updated = statement.executeUpdate()
        statement.close()
        connection.close()
        return updated > 0
    }

    boolean delete(Integer idVaga) {
        String sql = "DELETE FROM vaga WHERE id_vaga = ?"

        Connection connection = DatabaseConnection.getConnection()
        PreparedStatement statement = connection.prepareStatement(sql)
        statement.setInt(1, idVaga)

        int deleted = statement.executeUpdate()
        statement.close()
        connection.close()
        return deleted > 0
    }
}
