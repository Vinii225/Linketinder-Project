package groovy.data

import groovy.data.contracts.VagaRepository
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

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setInt(1, vaga.idEmpresa)
                statement.setString(2, vaga.nomeVaga)
                statement.setString(3, vaga.descricao)
                statement.setString(4, vaga.localizacao)

                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    resultSet.next()
                    vaga.idVaga = resultSet.getInt("id_vaga")
                }
            }
        }

        return vaga
    }

    List<Vaga> findAll() {
        String sql = """
            SELECT id_vaga, id_empresa, nome_vaga, descricao, localizacao
            FROM vaga
            ORDER BY id_vaga
        """.stripIndent()

        List<Vaga> vagas = []

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    while (resultSet.next()) {
                        vagas << mapVaga(resultSet)
                    }
                }
            }
        }

        return vagas
    }

    Vaga findById(Integer idVaga) {
        String sql = """
            SELECT id_vaga, id_empresa, nome_vaga, descricao, localizacao
            FROM vaga
            WHERE id_vaga = ?
        """.stripIndent()

        Vaga vaga = null

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setInt(1, idVaga)

                statement.executeQuery().withCloseable { ResultSet resultSet ->
                    if (resultSet.next()) {
                        vaga = mapVaga(resultSet)
                    }
                }
            }
        }

        return vaga
    }

    boolean update(Vaga vaga) {
        String sql = """
            UPDATE vaga
            SET id_empresa = ?, nome_vaga = ?, descricao = ?, localizacao = ?
            WHERE id_vaga = ?
        """.stripIndent()

        int updated = 0

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setInt(1, vaga.idEmpresa)
                statement.setString(2, vaga.nomeVaga)
                statement.setString(3, vaga.descricao)
                statement.setString(4, vaga.localizacao)
                statement.setInt(5, vaga.idVaga)

                updated = statement.executeUpdate()
            }
        }

        return updated > 0
    }

    boolean delete(Integer idVaga) {
        String sql = "DELETE FROM vaga WHERE id_vaga = ?"

        int deleted = 0

        DatabaseConnection.getConnection().withCloseable { Connection connection ->
            connection.prepareStatement(sql).withCloseable { PreparedStatement statement ->
                statement.setInt(1, idVaga)
                deleted = statement.executeUpdate()
            }
        }

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
