CREATE TABLE candidato (
    id_candidato SERIAL PRIMARY KEY, 
    nome VARCHAR(50),
    sobrenome VARCHAR(50),
    data_nasc DATE NOT NULL, 
    email VARCHAR(100) UNIQUE NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    pais VARCHAR(50),
    cep VARCHAR(10),
    descricao_pessoal TEXT,
    senha VARCHAR(255) NOT NULL 
);

CREATE TABLE competencia (
    id_competencia SERIAL PRIMARY KEY,
    nome_competencia VARCHAR(50) UNIQUE NOT NULL 
);

CREATE TABLE empresa ( 
    id_empresa SERIAL PRIMARY KEY,
    nome_empresa VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    email_corporativo VARCHAR(100) UNIQUE NOT NULL,
    descricao_empresa TEXT,
    pais VARCHAR(50),
    cep VARCHAR(10),
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE vaga ( 
	id_vaga SERIAL PRIMARY KEY,
    id_empresa INT NOT NULL REFERENCES empresa(id_empresa) ON DELETE CASCADE,
    nome_vaga VARCHAR(100) NOT NULL,
    descricao TEXT,
    localizacao VARCHAR(100)
);

CREATE TABLE candidato_competencia (
    id_candidato INT REFERENCES candidato(id_candidato) ON DELETE CASCADE,
    id_competencia INT REFERENCES competencia(id_competencia) ON DELETE CASCADE,
    PRIMARY KEY (id_candidato, id_competencia)
);

CREATE TABLE match (
    id_candidato INT REFERENCES candidato(id_candidato) ON DELETE CASCADE,
    id_empresa INT REFERENCES empresa(id_empresa) ON DELETE CASCADE,
    boolean_match BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id_candidato, id_empresa)
);



INSERT INTO competencia (nome_competencia) VALUES 
('Java'), ('Groovy'), ('PostgreSQL'), ('TypeScript'), ('React'), ('Docker'), ('AWS');

INSERT INTO candidato (nome, sobrenome, data_nasc, email, cpf, pais, cep, descricao_pessoal, senha) VALUES
('Marcos', 'Oliveira', '1995-03-12', 'marcos.dev@email.com', '101.202.303-44', 'Brasil', '58000-100', 'Desenvolvedor Backend focado em microserviços.', 'p4_secret123'),
('Julia', 'Costa', '1998-07-25', 'julia.tech@email.com', '505.606.707-88', 'Brasil', '58015-200', 'Especialista em Frontend e UI/UX.', 'react_queen'),
('Ricardo', 'Santos', '1990-11-05', 'ricardo.data@email.com', '909.101.202-33', 'Brasil', '58040-300', 'DBA com experiência em tunagem de queries.', 'sql_master90'),
('Beatriz', 'Lima', '2001-01-15', 'beatriz.cloud@email.com', '303.404.505-66', 'Brasil', '58050-400', 'Estudante de CC com foco em Cloud Computing.', 'aws_cloud_2026'),
('Fernando', 'Melo', '1993-09-20', 'fernando.full@email.com', '707.808.909-11', 'Brasil', '58060-500', 'Dev Fullstack entusiasta de Linux e Docker.', 'docker_linux_fan');

INSERT INTO empresa (nome_empresa, cnpj, email_corporativo, descricao_empresa, pais, cep, senha) VALUES
('Nexus Code', '22.333.444/0001-55', 'hr@nexuscode.io', 'Consultoria internacional de software.', 'Brasil', '01000-000', 'nexus_pass'),
('Stellar Systems', '66.777.888/0001-99', 'jobs@stellarsys.com', 'Desenvolvimento de soluções escaláveis em nuvem.', 'Brasil', '80000-000', 'stellar_2026'),
('BitLogic', '11.222.333/0001-44', 'talento@bitlogic.com.br', 'Startup focada em fintech e segurança.', 'Brasil', '50000-000', 'bit_secure'),
('DataWave', '44.555.666/0001-22', 'recrutamento@datawave.ai', 'Inteligência de dados e análise preditiva.', 'Brasil', '60000-000', 'wave_ai_99'),
('GreenStack', '88.999.000/0001-11', 'carreiras@greenstack.dev', 'Sistemas sustentáveis e tecnologia verde.', 'Brasil', '70000-000', 'eco_stack_pass');

INSERT INTO vaga (id_empresa, nome_vaga, descricao, localizacao) VALUES
(1, 'Engenheiro de Software Sênior', 'Atuar com arquitetura Java e Spring Boot.', 'Remoto'),
(2, 'Dev Ops Cloud', 'Gerenciamento de infraestrutura AWS.', 'São Paulo - SP'),
(3, 'Desenvolvedor Groovy Junior', 'Manutenção de sistemas legados e novos módulos.', 'Curitiba - PR'),
(4, 'Analista de Banco de Dados', 'Otimização de PostgreSQL e modelagem.', 'Remoto'),
(5, 'Estágio em Desenvolvimento', 'Apoio ao time de Fullstack (TS/React).', 'Florianópolis - SC');

INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES 
(1, 1), (1, 3), 
(2, 5), (2, 4),
(3, 3), (3, 6),
(4, 7), (4, 1),
(5, 2), (5, 6);

INSERT INTO match (id_candidato, id_empresa, boolean_match) VALUES 
(1, 1, TRUE),  
(3, 4, TRUE),  
(5, 3, FALSE); 



SELECT MIN(data_nasc) AS candidato_mais_velho, 
MAX(data_nasc) AS candidato_mais_novo, 
ROUND(AVG(id_candidato), 2) AS media_ids 
FROM candidato;

SELECT nome_empresa, email_corporativo
FROM empresa
WHERE id_empresa IN (
    SELECT id_empresa 
    FROM vaga
);

SELECT v.nome_vaga, v.localizacao, e.nome_empresa AS anunciante
FROM vaga v
INNER JOIN empresa e ON v.id_empresa = e.id_empresa
ORDER BY v.nome_vaga;

SELECT c.nome AS candidato, comp.nome_competencia AS habilidade
FROM candidato c
JOIN candidato_competencia cc ON c.id_candidato = cc.id_candidato
JOIN competencia comp ON cc.id_competencia = comp.id_competencia
WHERE comp.nome_competencia = 'Groovy';


