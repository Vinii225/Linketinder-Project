package groovy.data.contracts

import java.sql.Connection

interface CompetenciaLookup {
    Integer findOrCreateByName(String nomeCompetencia, Connection connection)
}
